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
  (testing "Should perform all trade operations and save it on storage"
    (let [operation0 {:operation "buy" :unit-cost 10.00 :quantity 100}
          operation1 {:operation "sell" :unit-cost 15.00 :quantity 50}
          operation2 {:operation "sell" :unit-cost 15.00 :quantity 50}
          storage (new-in-memory-storage)
          db-state0 {:weighted-avg 10.0 :quantity 100 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 50 :loss 0}
          db-state2 {:weighted-avg 10.0 :quantity 0 :loss 0}]
      (is (= (buy-stocks storage operation0) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (sell-stocks storage operation1) {:tax 0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage operation2) {:tax 0}))
      (is (= (db/get-state storage) db-state2)))))

(deftest case-2
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "sell", :unit-cost 20.00, :quantity 5000}
                      {:operation "sell", :unit-cost 5.00, :quantity 5000}]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 5000 :loss 0}
          db-state2 {:weighted-avg 10.0 :quantity 0 :loss 25000.0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (sell-stocks storage (second operations)) {:tax 10000.0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage (nth operations 2)) {:tax 0}))
      (is (= (db/get-state storage) db-state2)))))
(deftest case-3
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "sell", :unit-cost 5.00, :quantity 5000}
                      {:operation "sell", :unit-cost 20.00, :quantity 3000}]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 5000 :loss 25000.0}
          db-state2 {:weighted-avg 10.0 :quantity 2000 :loss 0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (sell-stocks storage (second operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage (nth operations 2)) {:tax 1000.0}))
      (is (= (db/get-state storage) db-state2)))))

(deftest case-4
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "buy", :unit-cost 25.00, :quantity 5000}
                      {:operation "sell", :unit-cost 15.00, :quantity 10000}]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 15.0 :quantity 15000 :loss 0}
          db-state2 {:weighted-avg 15.0 :quantity 5000 :loss 0.0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (buy-stocks storage (second operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage (nth operations 2)) {:tax 0}))
      (is (= (db/get-state storage) db-state2)))))

(deftest case-5
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "buy", :unit-cost 25.00, :quantity 5000}
                      {:operation "sell", :unit-cost 15.00, :quantity 10000}
                      {:operation "sell", :unit-cost 25.00, :quantity 5000}]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 15.0 :quantity 15000 :loss 0}
          db-state2 {:weighted-avg 15.0 :quantity 5000 :loss 0.0}
          db-state3 {:weighted-avg 15.0 :quantity 0 :loss 0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (buy-stocks storage (second operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage (nth operations 2)) {:tax 0}))
      (is (= (db/get-state storage) db-state2))
      (is (= (sell-stocks storage (nth operations 3)) {:tax 10000.0}))
      (is (= (db/get-state storage) db-state3)))))

(deftest case-6
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "sell", :unit-cost 2.00, :quantity 5000}
                      {:operation "sell", :unit-cost 20.00, :quantity 2000}
                      {:operation "sell", :unit-cost 20.00, :quantity 2000}
                      {:operation "sell", :unit-cost 25.00, :quantity 1000}]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 5000 :loss 40000.0}
          db-state2 {:weighted-avg 10.0 :quantity 3000 :loss 20000.0}
          db-state3 {:weighted-avg 10.0 :quantity 1000 :loss 0}
          db-state4 {:weighted-avg 10.0 :quantity 0 :loss 0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (sell-stocks storage (second operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage (nth operations 2)) {:tax 0}))
      (is (= (db/get-state storage) db-state2))
      (is (= (sell-stocks storage (nth operations 3)) {:tax 0.0}))
      (is (= (db/get-state storage) db-state3))
      (is (= (sell-stocks storage (nth operations 4)) {:tax 3000.0}))
      (is (= (db/get-state storage) db-state4)))))

(deftest case-7
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "sell", :unit-cost 2.00, :quantity 5000}
                      {:operation "sell", :unit-cost 20.00, :quantity 2000}
                      {:operation "sell", :unit-cost 20.00, :quantity 2000}
                      {:operation "sell", :unit-cost 25.00, :quantity 1000}
                      {:operation "buy", :unit-cost 20.00, :quantity 10000}
                      {:operation "sell", :unit-cost 15.00, :quantity 5000}
                      {:operation "sell", :unit-cost 30.00, :quantity 4350}
                      {:operation "sell", :unit-cost 30.00, :quantity 650}]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 5000 :loss 40000.0}
          db-state2 {:weighted-avg 10.0 :quantity 3000 :loss 20000.0}
          db-state3 {:weighted-avg 10.0 :quantity 1000 :loss 0}
          db-state4 {:weighted-avg 10.0 :quantity 0 :loss 0}
          db-state5 {:weighted-avg 20.0 :quantity 10000 :loss 0}
          db-state6 {:weighted-avg 20.0 :quantity 5000 :loss 25000.0}
          db-state7 {:weighted-avg 20.0 :quantity 650 :loss 0}
          db-state8 {:weighted-avg 20.0 :quantity 0 :loss 0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (sell-stocks storage (second operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (sell-stocks storage (nth operations 2)) {:tax 0}))
      (is (= (db/get-state storage) db-state2))
      (is (= (sell-stocks storage (nth operations 3)) {:tax 0.0}))
      (is (= (db/get-state storage) db-state3))
      (is (= (sell-stocks storage (nth operations 4)) {:tax 3000.0}))
      (is (= (db/get-state storage) db-state4))
      (is (= (buy-stocks storage (nth operations 5)) {:tax 0}))
      (is (= (db/get-state storage) db-state5))
      (is (= (sell-stocks storage (nth operations 6)) {:tax 0}))
      (is (= (db/get-state storage) db-state6))
      (is (= (sell-stocks storage (nth operations 7)) {:tax 3700.0}))
      (is (= (db/get-state storage) db-state7))
      (is (= (sell-stocks storage (nth operations 8)) {:tax 0}))
      (is (= (db/get-state storage) db-state8)))))

(deftest case-8
  (testing "Should perform all trade operations and save it on storage"
    (let [storage (new-in-memory-storage)
          operations [{:operation "buy", :unit-cost 10.00, :quantity 10000}
                      {:operation "sell", :unit-cost 50.00, :quantity 10000}
                      {:operation "buy", :unit-cost 20.00, :quantity 10000}
                      {:operation "sell", :unit-cost 50.00, :quantity 10000}
                      ]
          db-state0 {:weighted-avg 10.0 :quantity 10000 :loss 0}
          db-state1 {:weighted-avg 10.0 :quantity 0 :loss 0}
          db-state2 {:weighted-avg 20.0 :quantity 10000 :loss 0}
          db-state3 {:weighted-avg 20.0 :quantity 0 :loss 0}]
      (is (= (buy-stocks storage (first operations)) {:tax 0}))
      (is (= (db/get-state storage) db-state0))
      (is (= (sell-stocks storage (second operations)) {:tax 80000.0}))
      (is (= (db/get-state storage) db-state1))
      (is (= (buy-stocks storage (nth operations 2)) {:tax 0}))
      (is (= (db/get-state storage) db-state2))
      (is (= (sell-stocks storage (nth operations 3)) {:tax 60000.0}))
      (is (= (db/get-state storage) db-state3)))))

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
