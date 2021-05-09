(ns authorizer.router
  (:require [authorizer.controllers.account :as account-controller]
            [authorizer.controllers.transaction :as transaction-controller]))

(defn forward-operation
  [payload storage]
  (cond
    (.contains payload "account") (account-controller/create-account payload storage)
    (.contains payload "transaction") (transaction-controller/create-transaction! payload storage)))

