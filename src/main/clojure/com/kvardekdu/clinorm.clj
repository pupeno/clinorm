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