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

(def f (com.kvardekdu.clinorm.sql/connect {:type :derby :file-name "/tmp/blah.db" :create true}))

(com.kvardekdu.clinorm/defmodel :users
  [[:username :string]
   [:age :integer]
   [:email :string]])