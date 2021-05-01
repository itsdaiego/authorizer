(ns authorizer.controllers.account
  (:require [authorizer.adapter :as adapter]
            [authorizer.logic.account :as account-logic]
            [authorizer.protocols.storage-client :as storage-client]))

(defn create-account
  [payload storage]
  (let [payload (adapter/json_to_map payload)
        existing-account? (account-logic/account-duplicated? (storage-client/get-all storage))]
    (cond
      (true? existing-account?) (assoc payload :violations ["account-already-initialized"])
      :else (storage-client/create! storage 
                                    #(assoc % :account (:account payload) :violations [])))))
