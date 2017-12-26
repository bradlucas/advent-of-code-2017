(ns advent.day23.coprocessor
  (:require [clojure.string :as str]))


;; load instructions

;; run program and count the number of times the `mul` instruction is called


(def input-file "src/advent/day23/day23.txt")

;; @see https://rosettacode.org/wiki/Determine_if_a_string_is_numeric#Clojure
(defn numeric? [s]
  (if-let [s (seq s)]
    (let [s (if (= (first s) \-) (next s) s)
          s (drop-while #(Character/isDigit %) s)
          s (if (= (first s) \.) (next s) s)
          s (drop-while #(Character/isDigit %) s)]
      (empty? s))))

(defn convert-if-numeric
  "If the input is number return it as an integer else convert the string to a keyword"
  [s]
  (if (numeric? s)
    (Integer/parseInt s)
    (keyword s)))

(defn parse-line [l]
  (let [[a b c] (str/split l #" ")]
    {:cmd (keyword a)
     :reg (convert-if-numeric b)        ;; jnz can have a number in the middle slot
     :val (convert-if-numeric c)}
     ))

;; Instructions
;; --------------------------------------------------
;; set register value (reg or number)
;; sub register value (
;; mul register value
;; jnz x y   - jump offseat of y onluy if value in x is not zero
;;
;; Eight registers a through h all intiialized to 0


(defn set-value [registers reg val]
  (if (not (keyword? val))
    (assoc registers reg val)
    (assoc registers reg (val registers))))

(defn sub-value [registers reg val]
  (if (not (keyword? val))
    (assoc registers reg (- (reg registers) val))
    (assoc registers reg (- (reg registers) (val registers)))))

(defn mul-value [registers reg val]
  ;; (println "mul")
  (if (not (keyword? val))
    (assoc registers reg (* (reg registers) val))
    (assoc registers reg (* (reg registers) (val registers)))))

(defn jnz-value [registers reg val idx]
  (let [r
        (if (and (not (keyword? reg)) (not (= reg 0)))
          (+ idx val)
          (let [reg-value (reg registers)]
            (if (not (= reg-value 0))
              (+ idx val)
              (inc idx))))]
    r))

(defn load-program [s]
  (let [p (mapv parse-line (str/split s #"\n"))
        r (into {} (map (fn [n] {n 0}) [:a :b :c :d :e :f :g :h]))]
    [p r]))

(defn step [program registers idx cnt]
  ;; (println "idx" idx)
  (let [statement (get program idx)
        cmd (:cmd statement)]
    (let [[registers idx cnt] (cond
                                (= cmd :set) [(set-value registers (:reg statement) (:val statement)) (inc idx) cnt]
                                (= cmd :sub) [(sub-value registers (:reg statement) (:val statement)) (inc idx) cnt]
                                (= cmd :mul) [(mul-value registers (:reg statement) (:val statement)) (inc idx) (inc cnt)]
                                (= cmd :jnz) [registers (jnz-value registers (:reg statement) (:val statement) idx) cnt]
                                )]
      ;; (if (= cmd :jnz)
      ;; (println "jnz to new idx: " idx))
      ;; (println "step" cmd idx registers)
      [registers
       idx
       cnt]
      )))
          
          
(defn run [[program registers]]
  (let [max-cnt (count program)]
    (loop [[registers idx cnt] [registers 0 0]]
      (if (>= idx max-cnt)
        cnt        (recur (step program registers idx cnt))))))

(defn run-part1 []
  (let [s (slurp input-file)]
    (run (load-program s))))
;; 6241        


;; Part 2
;; a is set to 1
;; what is the value of h at the end


;; Analysis of algorithm
;;
;; Find the number of non-prime numbers between the value of b and c stepping by 17

;; step :set 7 {:a 1, :b 108100, :c 108100, :d 0, :e 0, :f 0, :g 0, :h 0}
;; ..
;; step :sub 19 {:a 1, :b 108100, :c 125100, :d 3, :e 13247, :f 0, :g -94853, :h 0}

(defn not-prime [n]
  (some #(= 0 (mod n %)) (range 2 n)))

(defn run-part2 []
  (let [b 108100
        c 125100]
    (count (filter not-prime (range b (inc c) 17)))))
;; 909


