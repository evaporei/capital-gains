(ns capital-gains.in-memory-storage-test
  (:require [clojure.test :refer [deftest is testing]]
            [capital-gains.storage :as storage]
            [capital-gains.in-memory-storage :refer [new-in-memory-storage]]))

(defn get-internal-storage [in-memory-storage]
  @(:storage in-memory-storage))

(deftest set-key-simple
  (testing "Should set a value in a key"
    (let [storage (new-in-memory-storage)
          new-key :cool-key
          new-value :cool-value
          expected {new-key new-value}]
      (is (= (storage/set-key! storage new-key new-value) expected))
      (is (= (get-internal-storage storage) expected)))))

(deftest update-key-simple
  (testing "Should update a key using an update function"
    (let [initial-storage {:quantity 20}
          storage (new-in-memory-storage initial-storage)
          expected {:quantity 40}]
      (is (= (storage/update-key! storage :quantity #(* % 2)) expected))
      (is (= (get-internal-storage storage) expected)))))

(deftest get-key-simple
  (testing "Should get the value of a key with default"
    (let [initial-storage {:quantity 20}
          storage (new-in-memory-storage initial-storage)]
      (is (= (storage/get-key storage :quantity :default) 20))
      (is (= (get-internal-storage storage) initial-storage))
      (is (= (storage/get-key storage :non-existing :default) :default))
      (is (= (get-internal-storage storage) initial-storage)))))
