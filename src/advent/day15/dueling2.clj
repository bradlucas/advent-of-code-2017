(ns advent.day15.dueling2)

;; Part 2                 
;;
;; Generator A looks for values that are multiples of 4
;; Generator B looks for values that are multiples of 8


(defn generator [factor multiple value]
  (loop [value value]
    (let [v (rem (* factor value) 2147483647)]
      (if (= (mod v multiple) 0)
        v
        (recur v)))))

;; (defn make-generator [factor starting-value multiple]
;;   (let [fn (partial generator factor)]
;;     (filter #(= (mod % multiple) 0) (iterate (partial generator factor) starting-value))))

(defn make-generator [factor starting-value multiple]
  (let [fn (partial generator factor)]
    (iterate (partial generator factor multiple) starting-value)))

(defn generator-a []
  (let [factor 16807
        starting-value 883
        multiple 4]
    (make-generator factor starting-value multiple)))

(defn generator-b []
  (let [factor 48271
        starting-value 879
        multiple 8]
    (make-generator factor starting-value multiple)))

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


(defn run-part2 []
  (count (filter true? (take 5000000 (run-generators)))))
;; 253
