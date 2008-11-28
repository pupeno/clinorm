;; This file is part of Clinorm.
;;
;; Clinorm is free software: you can redistribute it and/or modify it under the
;; terms of the GNU Lesser General Public License as published by the Free
;; Software Foundation, either version 3 of the License, or (at your option) any
;; later version.
;;
;; Clinorm is distributed in the hope that it will be useful, but WITHOUT ANY
;; WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
;; A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
;; details.
;;
;; You should have received a copy of the GNU Lesser General Public License
;; along with Clinorm.  If not, see <http://www.gnu.org/licenses/>.

(ns com.kvardekdu.clinorm.util
  (:import (java.util Properties)))

(defmacro error [msg]
  "Throw an exception with the message msg."
  `(throw (new Exception ~msg)))

(defn str- 
  "Same as str, but keywords don't end up with a prepended ':'"
  [obj]
  (if (keyword? obj) (name obj) (str obj)))

(defn key- [key-value]
  "Return a stringified key on a key-value pair."
  (str- (first key-value)))

(defn value- [key-value]
  "Return a stringified value on a key-value pair."
  (str- (second key-value)))

(defn as-properties [properties]
  "Given a map, return it as a Properties object."
  (let [properties-object (new Properties)]
    (doseq [property properties]
      (.setProperty properties-object 
		    (key- property) (value- property))
    properties-object)))

(defn str-concat [strs]
  "Concatenate a sequence of strings."
  (reduce str strs))

(defn str-join [delimiter strs]
  "Join a sequence of strings separated by a delimiter ignoring empty strings."
  (str-concat (interpose delimiter (filter #(not (empty? %)) strs))))