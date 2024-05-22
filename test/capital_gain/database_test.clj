(ns capital-gain.database-test
  (:require [clojure.test :refer [deftest is testing]]
            [capital-gain.database :as db]
            [capital-gain.in-memory-storage :refer [new-in-memory-storage]]))

(def sample-state {:weighted-avg 10.0, :quantity 10000, :loss 0})

(defn get-internal-storage [in-memory-storage]
  @(:storage in-memory-storage))

(deftest get-state-simple
  (testing "Should get the current state in storage"
    (let [storage (new-in-memory-storage sample-state)
          empty {:quantity 0 :weighted-avg 0 :loss 0}]
      (is (= (db/get-state storage) sample-state))
      (is (= (db/get-state (new-in-memory-storage)) empty)))))

(deftest save-purchase-simple
  (testing "Should save the stock purchase in storage"
    (let [trade {:weighted-avg 20.0
                 :quantity 5000}
          storage (new-in-memory-storage sample-state)
          expected {:weighted-avg 20.0
                    :quantity 15000
                    :loss 0}]
      (db/save-purchase! storage trade)
      (is (= (get-internal-storage storage) expected)))))

(deftest save-loss-simple
  (testing "Should save the loss in storage"
    (let [trade {:weighted-avg 5.0
                 :quantity 5000}
          loss 25000.0
          storage (new-in-memory-storage sample-state)
          expected {:weighted-avg 10.0
                    :quantity 5000
                    :loss loss}]
      (db/save-loss! storage loss trade)
      (is (= (get-internal-storage storage) expected)))))
