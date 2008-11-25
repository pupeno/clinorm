(ns com.kvardekdu.clinorm)

(defmacro defmodel [name & keys]
  (let [super-name (fn [template]
		     (symbol (format template name)))
	create-table (super-name "create-table-%s")
	drop-table (super-name "drop-table-%s")]
    `(do
       (defstruct ~name
	 :id ~@(map first keys))
       (defn ~create-table [])
       (defn ~drop-table []))))