(ns com.kvardekdu.clinorm.sql)

(import '(java.util Properties))

(def connections (ref {}))

(def default-connection (ref nil))

(defn- str- 
  "Same as str, but keywords don't end up with a prepended ':'"
  [obj]
  (if (keyword? obj) (name obj) (str obj)))

(defn- key- [key-value]
  (str- (first key-value)))

(defn- value- [key-value]
  (str- (second key-value)))

(defn- set-connection [connection name default]
  (dosync
   (when default
     (ref-set default-connection connection))
  (ref-set connections (assoc @connections name connections))))

(defn- as-properties [properties]
  (let [properties-object (new Properties)]
    (doseq [property properties]
      (.setProperty properties-object 
		    (key- property)) (value- property))
    properties-object))

(defn- str-concat [strs]
  (reduce str strs))

(defn- jdbc-url [subprotocol subname options]
  (let [options (str-concat (map (fn [x] (format ";%s=%s" (key- x) (value- x))) options))]
    (format "jdbc:%s:%s%s" (str- subprotocol) subname options)))

(defmulti connect
  (fn [spec & rest]
    (:type spec)))

(defmethod connect :derby 
  ([spec default name]
     (assert (:file-name spec))  ;; TODO: add support for messages to assert.
     (println "Connecting to derby...")
     (let [subprotocol "derby"
	   subname (:file-name spec)
	   url (jdbc-url subprotocol subname nil)
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