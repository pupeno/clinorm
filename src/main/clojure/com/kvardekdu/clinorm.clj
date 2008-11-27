(ns com.kvardekdu.clinorm
  (:use com.kvardekdu.clinorm.util)
  (:require [com.kvardekdu.clinorm.sql :as sql]))

(defmacro defmodel [name fields]
  (let [super-name (fn [template]
		     (symbol (format template (str- name))))
	create-table (super-name "create-table-%s")
	drop-table (super-name "drop-table-%s")]
    `[(defstruct ~(symbol (str- name))
	  ~@(map first fields))
	(defn ~create-table []
	  (sql/create-table ~name ~fields))
	(defn ~drop-table []
	 (sql/drop-table ~name))]))