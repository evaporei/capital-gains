(ns capital-gain.business-logic)

(defn calculate-weighted-avg
  [curr-state trade]
  (let [curr-quantity (:quantity curr-state)
        curr-weighted-avg (:weighted-avg curr-state)
        new-quantity (:quantity trade)
        new-unit-cost (:unit-cost trade)]
    (/ (+ (* curr-quantity curr-weighted-avg) (* new-quantity new-unit-cost))
       (+ curr-quantity new-quantity))))

(defn calculate-tax
  [total-amount profit loss]
  (if (> total-amount 20000)
    (* (- profit loss) 0.2)
    0))

(defn calculate-loss
  [weighted-avg new-cost loss quantity]
  (let [total-amount (* new-cost quantity)
        profit (* quantity (- new-cost weighted-avg))]
    (if (or (<= new-cost weighted-avg) ;; loss
            (<= total-amount loss)) ;; profit, need to subtract loss
      {:new-loss (- loss profit)
       :tax 0}
      ;; overcome prev loss, pay tax
      {:new-loss 0
       :tax (calculate-tax total-amount profit loss)})))
