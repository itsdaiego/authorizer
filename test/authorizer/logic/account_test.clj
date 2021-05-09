(ns authorizer.logic.account-test
  (:require [authorizer.logic.account :refer [is-account-initialized? is-card-active? set-account-new-limit]]
            [clojure.test :refer [deftest testing is]]))

(deftest account-is-initialized
  (testing "should return true if account already exists"
    (let [account {:account {:active-card true, :available-limit 750}, :violations []}
          expected true]
      (is (= (is-account-initialized? account) expected)))))

(deftest account-is-not-initialized
  (testing "should return false if account does not exists"
    (let [account nil
          expected false]
      (is (= (is-account-initialized? account) expected)))))

(deftest card-is-active
  (testing "should return true if card is active"
    (let [account {:active-card true, :available-limit 750}
          expected true]
      (is (= (is-card-active? account) expected)))))

(deftest card-is-not-active
  (testing "should return false if card is not active"
    (let [account {:active-card false, :available-limit 750}
          expected false]
      (is (= (is-card-active? account) expected)))))

(deftest set-new-limit-to-account
  (testing "should return new available limit to account based on transaction's amount"
    (let [account {:active-card false, :available-limit 750}
          transaction {:merchant "Burger Queen", :amount 50, :time "2021-02-13T11:00:00.000Z"}
          expected {:active-card false, :available-limit 700}]
      (is (= (set-account-new-limit account transaction) expected)))))
