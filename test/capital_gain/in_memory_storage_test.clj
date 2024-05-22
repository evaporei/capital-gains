(ns capital-gain.in-memory-storage-test
  (:require [clojure.test :refer [deftest is testing]]
            [capital-gain.storage :as storage]
            [capital-gain.in-memory-storage :refer [new-in-memory-storage]]))

(defn get-internal-storage [in-memory-storage]
  @(:storage in-memory-storage))

(deftest insert-key-simple
  (testing "Should insert a value in a key"
    (let [storage (new-in-memory-storage)
          new-key :cool-key
          new-value :cool-value
          expected {new-key new-value}]
      (is (= (storage/insert-key! storage new-key new-value) expected))
      (is (= (get-internal-storage storage) expected)))))

(deftest update-key-simple
  (testing "Should update a key using an update function"
    (let [initial-storage {:quantity 20}
          storage (new-in-memory-storage initial-storage)
          expected {:quantity 40}]
      (is (= (storage/update-key! storage :quantity #(* % 2)) expected))
      (is (= (get-internal-storage storage) expected)))))

(deftest get-key-simple
  (testing "Should get the value of a key"
    (let [initial-storage {:quantity 20}
          storage (new-in-memory-storage initial-storage)]
      (is (= (storage/get-key storage :quantity) 20))
      (is (= (get-internal-storage storage) initial-storage)))))

(deftest get-key-with-default-simple
  (testing "Should get the value of a key with default"
    (let [initial-storage {:quantity 20}
          storage (new-in-memory-storage initial-storage)]
      (is (= (storage/get-key-with-default storage :quantity :default) 20))
      (is (= (get-internal-storage storage) initial-storage))
      (is (= (storage/get-key-with-default storage :non-existing :default) :default))
      (is (= (get-internal-storage storage) initial-storage)))))

(deftest has-key-simple
  (testing "Should check if key exists"
    (let [initial-storage {:quantity 20}
          storage (new-in-memory-storage initial-storage)]
      (is (= (storage/has-key? storage :quantity) true))
      (is (= (get-internal-storage storage) initial-storage))
      (is (= (storage/has-key? storage :non-existing) false))
      (is (= (get-internal-storage storage) initial-storage)))))
