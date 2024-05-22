(ns capital-gain.controller-test
  (:require [clojure.test :refer [deftest is testing]]
            [capital-gain.controller :refer [controller
                                             buy-stocks
                                             sell-stocks 
                                             execute-controller
                                             routing]]
            [capital-gain.database :as db]
            [capital-gain.in-memory-storage :refer [new-in-memory-storage]]))

(deftest case-1
  (testing "Should perform all account operations and save it on storage"
    (let [operation0 {:operation "buy" :unit-cost 10.00 :quantity 100}
          operation1 {:operation "sell" :unit-cost 15.00 :quantity 50}
          operation2 {:operation "sell" :unit-cost 15.00 :quantity 50}
          storage (new-in-memory-storage)
          db-state0 {:weighted-avg 10.0 :quantity 100 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 50 :loss 0}
          db-state2 {:weighted-avg 10.0 :quantity 0 :loss 0}]
      (is (= (buy-stocks storage operation0) {:tax 0}))
      (is (= (db/get-all storage) db-state0))
      (is (= (sell-stocks storage operation1) {:tax 0}))
      (is (= (db/get-all storage) db-state1))
      (is (= (sell-stocks storage operation2) {:tax 0}))
      (is (= (db/get-all storage) db-state2)))))

(deftest controller-integration
  (testing "Should perform all given operations and return the results in JSON format"
    (let [storage (new-in-memory-storage)
          operations "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},\n
                       {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000}]\n"
          expected "[{\"tax\":0},{\"tax\":10000.0}]\n"]
      (is (= (controller storage operations) expected)))))

(deftest integration-else
  (testing "Should perform nothing and return a null"
    (is (= (controller (new-in-memory-storage) "[{\"nothing\":\"related\"}]") "[null]\n"))))

(deftest routing-create-account
  (testing "Should return buy-stocks along with input-data"
    (is (= (routing {:operation "buy"}) [buy-stocks {:operation "buy"}]))))

(deftest routing-authorize-transaction
  (testing "Should return sell-stocks along with input-data"
    (is (= (routing {:operation "sell"}) [sell-stocks {:operation "sell"}]))))

(deftest routing-else
  (testing "Should return fn that returns nil along with input-data"
    (let [[controller-fn data] (routing {:nothing :related})]
      (is (= (controller-fn) nil))
      (is (= data {:nothing :related})))))

;; (deftest execute-controller-with-fn
;;   (testing "Should return fn passed with storage and input-data"
;;     (let [result (execute-controller {:storage {}} [(constantly {:a :b})])]
;;       (is (= result {:a :b})))))
