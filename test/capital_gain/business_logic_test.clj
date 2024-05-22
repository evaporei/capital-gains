(ns capital-gain.business-logic-test
    (:require [clojure.test :refer [deftest is testing]]
              [capital-gain.business-logic :refer [calculate-weighted-avg
                                                   calculate-loss]]))

(deftest calculate-weighted-avg-wout-curr
  (testing "Should calculate weighted average with empty current values"
    (let [curr {:quantity 0
                :weighted-avg 0}
          trade {:quantity 100
                 :unit-cost 10.0}
          expected 10.0]
      (is (= (calculate-weighted-avg curr trade) expected)))))

;; (deftest calculate-weighted-avg-w-curr
;;   (testing "Should calculate weighted average when there are current values"
;;     (let [curr {:quantity 5
;;                 :weighted-avg 20.0}
;;           trade {:quantity 5
;;                  :unit-price 10.0}
;;           expected 15.0]
;;       (is (= (calculate-weighted-avg curr trade) expected)))))
;;
;; (deftest sell-stock-test
;;   (testing "ashtt"
;;     (let [curr {:quantity 5
;;                 :weighted-avg 20.0}
;;           trade {:quantity 5
;;                  :unit-price 10.0}
;;           expected 15.0]
;;       (is (= (calculate-weighted-avg curr trade) expected)))))
