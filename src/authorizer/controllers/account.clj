(ns authorizer.controllers.account
  (:require [authorizer.adapter :as adapter]
            [authorizer.logic.account :as account-logic]
            [authorizer.db.account :as account-db]))

(defn create-account
  [payload storage]
  (let [payload (adapter/json-to-hmap payload)
        is-account-initialized? (account-logic/is-account-initialized? (account-db/select-all storage))]
    (cond
      is-account-initialized? (adapter/hmap-to-json (assoc payload :violations ["account-already-initialized"]))
      :else (adapter/hmap-to-json (account-db/create! storage {:account (:account payload) :violations []})))))
