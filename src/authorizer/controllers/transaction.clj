(ns authorizer.controllers.transaction
  (:require [authorizer.adapter :as adapter]
            [authorizer.logic.account :refer [is-account-initialized? is-card-active?]]
            [authorizer.logic.transaction :refer [has-sufficient-funds? has-high-frequency-transactions? has-doubled-transactions?]]
            [authorizer.db.account :as account-db]
            [authorizer.db.transaction :as transaction-db]))

(defn perform-authorization-validation
  [transactions account]
  (let [current-transaction (first transactions)
        violations []]
    (cond
      (not (is-account-initialized? account)) (conj violations "account-not-initialized")
      (not (is-card-active? account)) (conj violations "card-inactive")
      (not (has-sufficient-funds? account current-transaction)) (conj violations "insufficient-limit")
      (has-high-frequency-transactions? transactions) (conj violations "high-frequency-small-interval")
      (has-doubled-transactions? transactions) (conj violations "double-transaction"))))

(defn create!
  [transactions account storage]
  (let [current-transaction (first transactions)
        account-with-new-amount (assoc account :available-limit (- (:available-limit account) (:amount current-transaction)))]
    (transaction-db/create! storage current-transaction)
    (account-db/create! storage {:account account-with-new-amount})
    {:account account-with-new-amount}))

(defn create-transaction!
  [payload storage]
  (let [transaction (:transaction (adapter/json-to-map payload))
        transactions (conj (transaction-db/select-all storage) transaction) ;; TODO: get transactions from last minute only
        account (account-db/select-all storage)
        authorization-validation (perform-authorization-validation transactions account)]

    (if (nil? authorization-validation)
      (-> (create! transactions account storage))
      (assoc {} :account (if (nil? account) {} account) :violations authorization-validation))))
