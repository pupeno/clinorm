(ns com.kvardekdu.clinorm.sql
  (:use com.kvardekdu.clinorm.util)
  (:require [com.kvardekdu.clinorm.connections :as connections])
  (:import (java.sql Connection)))

(defn- get-connection [connection]
  (dosync
   (cond
    (nil? connection) @connections/default
    (contains? @connections/all connection) (get @connections/all connection)
    (instance? Connection connection) connection
    :else (error "Connection not found."))))

(defn- set-connection [connection name default]
  (dosync
   (when default
     (ref-set connections/default connection))
  (ref-set connections/all (assoc @connections/all name connection))))

(defn- jdbc-url [subprotocol subname options]
  (let [options (str-concat (map #(format ";%s=%s" (key- %) (value- %)) 
				 (filter #(not (nil? (second %))) options)))]
    (format "jdbc:%s:%s%s" (str- subprotocol) subname options)))

(defmulti connect
  (fn [spec & rest]
    (:type spec)))

;; Connect to derby. The spec must contain:
;;   file-name: the filename of the db.
;; The spec may contain:
;;   create: when true, the database will be created.
;;   any other item will be passed as argument in the JDBC url, like :shutdown true => ;shutdown.
(defmethod connect :derby
  ([spec default name]
     (assert (:file-name spec))  ;; TODO: add support for messages to assert.
     (println "Connecting to derby...")
     (let [subprotocol "derby"
	   subname (:file-name spec)
	   options (dissoc spec :type :file-name)
	   url (jdbc-url subprotocol subname options)
	   props (as-properties {:classname "org.apache.derby.jdbc.EmbeddedDriver"
				 :subprotocol subprotocol
				 :subname subname})
	   connection (java.sql.DriverManager/getConnection url)]
       (set-connection connection name default)
       connection))
  ;; TODO: move the next 2 cases to another more generic method by improving the dispatch function.
  ([spec default]
     (connect spec default :derby))
  ([spec]
     (connect spec true)))

;(defmethod connect :mysql [spec]
;  (println "Connecting to mysql."))

(defn- get-statement [connection]
  (.createStatement (get-connection connection)))

(defn- type-to-sql-type [type]
  (cond
   (= type :boolean) "BIT"
   (= type :integer) "BIGINT"
   (= type :float) "DOUBLE"
   (= type :decimal) "DECIMAL"
   (= type :string) "VARCHAR(255)" ; TODO: this is a hack, it doesn't work, it doesn't let you define the number for varchar. FIXME!
   (= type :date) "DATE"
   (= type :time) "TIME"
   (= type :timestamp) "TIMESTAMP"
   (= type :blob) "BLOB"
   (vector? type) (format "%s(%s)" (type-to-sql-type (first type)) (second type))))

(defn- option-to-sql-option [option]
  (cond
   (= option :auto-increment) "GENERATED ALWAYS AS IDENTITY"
   :else ""))

(defn- field-to-sql [[name type & rest]]
  (str-join " " [(str- name)
		 (type-to-sql-type type)
		 (str-join ", " (map option-to-sql-option rest))]))

(defn- create-table-sql [name fields]
  (format "CREATE TABLE %s (\n  %s\n)" (str- name) (str-join ",\n  " (map field-to-sql fields))))

(defn- drop-table-sql [name]
  (format "DROP TABLE %s" (str- name)))

(defn create-table 
  ([name fields connection]
     (let [statement (get-statement connection)]
       (.executeUpdate statement (create-table-sql name fields))))
  ([name fields]
     (create-table name fields nil)))

(defn drop-table 
  ([name connection]
     (let [statement (get-statement connection)]
       (.executeUpdate statement (drop-table-sql name))))
  ([name]
     (drop-table name nil)))