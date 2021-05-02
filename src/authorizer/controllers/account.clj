(ns authorizer.controllers.account
  (:require [authorizer.adapter :as adapter]
            [authorizer.logic.account :as account-logic]
            [authorizer.protocols.storage-client :as storage-client]))

(defn create-account
  [payload storage]
  (let [payload (adapter/json-to-map payload)
        is-account-initialized? (account-logic/is-account-initialized? (storage-client/get-all storage))]
    (cond
      is-account-initialized? (assoc payload :violations ["account-already-initialized"])
      :else (storage-client/create! storage
                                    #(assoc % :account (:account payload) :violations [])))))
