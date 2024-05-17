(ns capital-gain.business-logic)

(defn calculate-weighted-avg
  "new-weighted-avg = ((curr-quantity * curr-weighted-avg) + (new-quantity * new-unit-price)) / (curr-quantity + new-quantity)"
  [curr trade]
  (let [curr-quantity (:quantity curr)
        curr-weighted-avg (:weighted-avg curr)
        new-quantity (:quantity trade)
        new-unit-cost (:unit-cost trade)]
    (/ (+ (* curr-quantity curr-weighted-avg) (* new-quantity new-unit-cost))
       (+ curr-quantity new-quantity))))

(defn calculate-tax
  [new-cost weighted-avg quantity total-amount loss]
  (let [profit (* quantity (- new-cost weighted-avg))]
    (if (> total-amount 20000)
      (* (- profit loss) 0.2)
      0)))

(defn sell-stock
  [weighted-avg new-cost loss quantity]
  (let [total-amount (* new-cost quantity)
        profit (* quantity (- new-cost weighted-avg))]
    ;; (print "total-amount:")
    ;; (println total-amount)
    (if (<= new-cost weighted-avg)
      ;; loss
      {:new-loss (- loss profit)
       :tax 0}
      ;; profit
      (if (<= total-amount loss)
        ;; just subtract from prev loss
        {:new-loss (- loss profit)
         :tax 0}
        ;; overcome prev loss, pay tax
        {:new-loss 0
         :tax (calculate-tax new-cost
                             weighted-avg
                             quantity
                             total-amount
                             loss)}))))
