(ns advent.day15.dueling)



;; The generators both work on the same principle. To create its next value,
;; a generator will take the previous value it produced, multiply it by a
;; factor (generator A uses 16807; generator B uses 48271), and then keep
;; the remainder of dividing that resulting product by 2147483647. That
;; final remainder is the value it produces next.
                                                                                                                                                                   


(defn generator [factor value]
  (rem (* factor value) 2147483647))

(defn make-generator [factor starting-value]
  (let [fn (partial generator factor)]
    (iterate (partial generator factor) starting-value)))

;; (defn generator-a []
;;   (let [factor 16807
;;         starting-value 65]
;;     (make-generator factor starting-value)))

;; (defn generator-b []
;;   (let [factor 48271
;;         starting-value 8921]
;;     (make-generator factor starting-value)))


(defn generator-a []
  (let [factor 16807
        starting-value 883]
    (make-generator factor starting-value)))

(defn generator-b []
  (let [factor 48271
        starting-value 879]
    (make-generator factor starting-value)))

(defn extract-lower-16 [x]
  (bit-and x (unchecked-dec (bit-shift-left 1 16))))
  
(defn lower-16-match [a b]
  ;; convert to binary
  ;; extract lower 16 bits
  (let [a1 (extract-lower-16 (unchecked-int a))
        b1 (extract-lower-16 (unchecked-int b))]
    (= a1 b1)))

#_ (lower-16-match 245556042 1431495498)


(defn run-generators []
  (let [fna (generator-a)
        fnb (generator-b)]
    (map (fn [a b] (lower-16-match a b)) fna fnb)))


(defn run-part1 []
  (count (filter true? (take 40000000 (run-generators)))))



;; Part 1
;; Generator A starts with 883
;; Generator B starts with 879



;; Part 2

