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
  [new-cost weighted-avg quantity total-amount]
  (if (> total-amount 20000)
    (* (* quantity (- new-cost weighted-avg)) 0.2)
    0))

(defn sell-stock
  [weighted-avg new-cost loss quantity]
  (let [total-amount (* new-cost quantity)]
    (if (<= new-cost weighted-avg)
      ;; loss
      {:new-loss (- total-amount loss)
       :tax 0}
      ;; profit
      (if (<= total-amount loss)
        ;; just subtract from prev loss
        {:new-loss (- loss total-amount)
         :tax 0}
        ;; overcome prev loss, pay tax
        {:new-loss 0
         :tax (calculate-tax new-cost
                             weighted-avg
                             quantity
                             total-amount)}))))
