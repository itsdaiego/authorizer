(ns authorizer.logic.account-test
  (:require [authorizer.logic.account :refer [account-duplicated?]]
            [clojure.test :refer [deftest testing is]]))

(deftest has-duplicated-account
  (testing "should return true if account already exists"
    (let [input {:account {:active-card true, :available-limit 750}, :violations []}
         expected true]
    (is (= (account-duplicated? input) expected)))))

(deftest is-new-account
  (testing "should return false if account does not exists"
    (let [input {}
          expected false]
      (is (= (account-duplicated? input) expected)))))
