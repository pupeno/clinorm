(def f (com.kvardekdu.clinorm.sql/connect {:type :derby :file-name "/tmp/blah.db" :create true}))

(com.kvardekdu.clinorm/defmodel :users
  [[:username :string]
   [:age :integer]
   [:email :string]])