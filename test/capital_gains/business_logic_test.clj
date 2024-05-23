(ns capital-gains.business-logic-test
  (:require [clojure.test :refer [deftest is testing]]
            [capital-gains.business-logic :refer [calculate-weighted-avg
                                                 calculate-tax
                                                 calculate-loss]]))

(deftest calculate-weighted-avg-without-curr
  (testing "Should calculate weighted average with empty current values"
    (let [curr {:quantity 0
                :weighted-avg 0}
          trade {:quantity 100
                 :unit-cost 10.0}
          expected 10.0]
      (is (= (calculate-weighted-avg curr trade) expected)))))

(deftest calculate-weighted-avg-with-curr
  (testing "Should calculate weighted average when there are current values"
    (let [curr {:quantity 5
                :weighted-avg 20.0}
          trade {:quantity 5
                 :unit-cost 10.0}
          expected 15.0]
      (is (= (calculate-weighted-avg curr trade) expected)))))

(deftest calculate-tax-with-profit
  (testing "Should give proper tax given a profit"
    (let [weighted-avg 10.0
          quantity 5000
          new-cost 20.0
          total-amount (* new-cost quantity)
          profit (* quantity (- new-cost weighted-avg))
          loss 25000.0
          expected 5000.0]
      (is (= (calculate-tax total-amount profit loss) expected)))))

(deftest calculate-tax-with-loss
  (testing "Should give no tax given a loss"
    (let [weighted-avg 10.0
          quantity 1000
          new-cost 5.0
          total-amount (* new-cost quantity)
          profit (* quantity (- new-cost weighted-avg))
          loss 500.0
          expected 0]
      (is (= (calculate-tax total-amount profit loss) expected)))))

(deftest calculate-loss-direct-loss
  (testing "Should handle direct loss correctly"
    (let [weighted-avg 15.0
          new-cost 10.0
          loss 5000.0
          quantity 400
          expected 7000.0]
      (is (= (calculate-loss weighted-avg new-cost loss quantity)
             {:new-loss expected
              :tax 0})))))

(deftest calculate-loss-adjusted-by-profit
  (testing "Should adjust loss by profit when total amount is less than loss"
    (let [weighted-avg 20.0
          new-cost 25.0
          loss 30000.0
          quantity 1000
          expected 25000.0]
      (is (= (calculate-loss weighted-avg new-cost loss quantity)
             {:new-loss expected
              :tax 0})))))

(deftest calculate-loss-with-profit-and-tax
  (testing "Should handle situations with no loss adjustment and tax calculation"
    (let [weighted-avg 10.0
          new-cost 20.0
          loss 15000.0
          quantity 2000
          profit (* quantity (- new-cost weighted-avg))
          total-amount (* new-cost quantity)
          expected-tax (calculate-tax total-amount profit loss)]
      (is (= (calculate-loss weighted-avg new-cost loss quantity)
             {:new-loss 0
              :tax expected-tax})))))
