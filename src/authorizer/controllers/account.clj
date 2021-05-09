(ns authorizer.controllers.account
  (:require [authorizer.adapter :as adapter]
            [authorizer.logic.account :as account-logic]
            [authorizer.db.account :as account-db]))

(defn create-account
  [payload storage]
  (let [payload (adapter/json-to-map payload)
        is-account-initialized? (account-logic/is-account-initialized? (account-db/select-all storage))]
    (cond
      is-account-initialized? (assoc payload :violations ["account-already-initialized"])
      :else (account-db/create! storage {:account (:account payload) :violations []}))))
