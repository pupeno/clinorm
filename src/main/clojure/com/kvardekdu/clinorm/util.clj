(ns com.kvardekdu.clinorm.util
  (:import (java.util Properties)))

(defmacro error [msg]
  "Throw an exception with the message msg."
  `(throw (new Exception ~msg)))

(defn- str- 
  "Same as str, but keywords don't end up with a prepended ':'"
  [obj]
  (if (keyword? obj) (name obj) (str obj)))

(defn- key- [key-value]
  "Return a stringified key on a key-value pair."
  (str- (first key-value)))

(defn- value- [key-value]
  "Return a stringified value on a key-value pair."
  (str- (second key-value)))

(defn- as-properties [properties]
  "Given a map, return it as a Properties object."
  (let [properties-object (new Properties)]
    (doseq [property properties]
      (.setProperty properties-object 
		    (key- property) (value- property))
    properties-object)))

(defn- str-concat [strs]
  "Concatenate a sequence of strings."
  (reduce str strs))

(defn- str-join [delimiter strs]
  "Join a sequence of strings separated by a delimiter ignoring empty strings."
  (str-concat (interpose delimiter (filter #(not (empty? %)) strs))))