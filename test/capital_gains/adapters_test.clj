(ns capital-gains.adapters-test
  (:require [clojure.test :refer [deftest is testing]]
            [capital-gains.adapters :refer [json->edn edn->json]]))

(deftest json-to-edn-simple
  (testing "Should return simple EDN structure"
    (let [json "{\"very-cool\":\"data\"}"
          expected-edn {:very-cool "data"}]
      (is (= (json->edn json) expected-edn)))))

(deftest edn-to-json-simple
  (testing "Should return simple JSON String"
    (let [edn {:very-cool "data"}
          expected-json-str "{\"very-cool\":\"data\"}\n"]
      (is (= (edn->json edn) expected-json-str)))))
