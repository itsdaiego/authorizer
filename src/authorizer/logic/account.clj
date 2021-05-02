(ns authorizer.logic.account)

(defn is-account-initialized?
  [account]
  (not= (:account account) nil))

(defn is-card-active?
  [account]
  (= (:active-card account) true))
