(ns authorizer.logic.account)

(defn is-account-initialized?
  [account]
  (not= account nil))

(defn is-card-active?
  [account]
  (= (:active-card account) true))

(defn set-account-new-limit
  [account transaction]
  (assoc account :available-limit (- (:available-limit account) (:amount transaction))))
