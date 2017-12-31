(ns advent.day25.halting)

;; Tape is an infinite list of numbers with values 0 or 1 and a
;; cursor into a position on the tape
;;
;; Checksum of tape is the number of 1s on the tape


;; In state A:
;;   If the current value is 0:
;;     - Write the value 1.
;;     - Move one slot to the right.
;;     - Continue with state B.
;;   If the current value is 1:
;;     - Write the value 0.
;;     - Move one slot to the left.
;;     - Continue with state F.
(defn state-a [tape pos]
  (if (= 0 (get tape pos))
    [(assoc tape pos 1) (inc pos) :b]
    [(assoc tape pos 0) (dec pos) :f]
    ))

;; In state B:
;;   If the current value is 0:
;;     - Write the value 0.
;;     - Move one slot to the right.
;;     - Continue with state C.
;;   If the current value is 1:
;;     - Write the value 0.
;;     - Move one slot to the right.
;;     - Continue with state D.
(defn state-b [tape pos]
  (if (= 0 (get tape pos))
    [(assoc tape pos 0) (inc pos) :c]
    [(assoc tape pos 0) (inc pos) :d]
    ))

;; In state C:
;;   If the current value is 0:
;;     - Write the value 1.
;;     - Move one slot to the left
;;     - Continue with state D.
;;   If the current value is 1:
;;     - Write the value 1.
;;     - Move one slot to the right.
;;     - Continue with state E.
(defn state-c [tape pos]
  (if (= 0 (get tape pos))
    [(assoc tape pos 1) (dec pos) :d]
    [(assoc tape pos 1) (inc pos) :e]
    ))

;; In state D:
;;   If the current value is 0:
;;     - Write the value 0.
;;     - Move one slot to the left.
;;     - Continue with state E.
;;   If the current value is 1:
;;     - Write the value 0.
;;     - Move one slot to the left.
;;     - Continue with state D.
(defn state-d [tape pos]
  (if (= 0 (get tape pos))
    [(assoc tape pos 0) (dec pos) :e]
    [(assoc tape pos 0) (dec pos) :d]
    ))

;; In state E:
;;   If the current value is 0:
;;     - Write the value 0.
;;     - Move one slot to the right.
;;     - Continue with state A.
;;   If the current value is 1:
;;     - Write the value 1.
;;     - Move one slot to the right.
;;     - Continue with state C.
(defn state-e [tape pos]
  (if (= 0 (get tape pos))
    [(assoc tape pos 0) (inc pos) :a]
    [(assoc tape pos 1) (inc pos) :c]
    ))

;; In state F:
;;   If the current value is 0:
;;     - Write the value 1.
;;     - Move one slot to the left.
;;     - Continue with state A.
;;   If the current value is 1:
;;     - Write the value 1.
;;     - Move one slot to the right.
;;     - Continue with state A.

(defn state-f [tape pos]
  (if (= 0 (get tape pos))
    [(assoc tape pos 1) (dec pos) :a]
    [(assoc tape pos 1) (inc pos) :a]
    ))


(defn step [tape pos state]
  (cond
    (= state :a) (state-a tape pos)
    (= state :b) (state-b tape pos)
    (= state :c) (state-c tape pos)
    (= state :d) (state-d tape pos)
    (= state :e) (state-e tape pos)
    (= state :f) (state-f tape pos))
  )

(defn checksum [tape]
  ;; (println tape)
  (count (filter #(= 1 %) tape)))

(defn run [max-cnt]
  (loop [[tape pos state] [(mapv (fn [x] 0) (range (* 2 max-cnt))) max-cnt :a]
         cnt 0]
    ;; (println tape pos state)
    (if (>= cnt max-cnt)
      (checksum tape)
      ;; tape
      (recur (step tape pos state) (inc cnt)))))

(defn run-part1 []
  (run 12794428))
;; 2832

(defn run-part2 []
  "reboot printer")
;; reboot printer
