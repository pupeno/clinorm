(ns com.kvardekdu.clinorm.util)

(defmacro error [msg]
  `(throw (new Exception ~msg)))