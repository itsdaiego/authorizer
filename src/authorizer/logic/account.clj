(ns authorizer.logic.account)

(defn account-duplicated?
  [account]
  (cond
    (= account {}) false
    :else true))
