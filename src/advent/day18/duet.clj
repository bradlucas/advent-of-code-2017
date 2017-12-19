(ns advent.day18.duet
  (:require [clojure.string :as str]))


;; | snd X   | plays a sound with a frequency equal to the value of X.                                                                                            |
;; | set X Y | sets register X to the value of Y.                                                                                                                 |
;; | add X Y | increases register X by the value of Y.                                                                                                            |
;; | mul X Y | sets register X to the result of multiplying the value contained in register X by the value of Y.                                                  |
;; | mod X Y | sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y). |
;; | rcv X   | recovers the frequency of the last sound played, but only when the value of X is not zero. (If it is zero, the command does nothing.)              |
;; | jgz X Y | jumps with an offset of the value of Y, but only if the value of X is greater than zero.                                                           |
;; |         | (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)                                         |
;;
;; NOTE: Values can be integers or a register name

(def program-text (str/trim (slurp "src/advent/day18/day18.txt")))

(def test-program-text "set a 1\nadd a 2\nmul a a\nmod a 5\nsnd a\nset a 0\nrcv a\njgz a -1\nset a 1\njgz a -2")

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
    (if c
    {:cmd (keyword a)
     :reg (keyword b)
     :val (convert-if-numeric c)}
    {:cmd (keyword a)
     :val (convert-if-numeric b)})))

(defn load-program [s]
  (let [program (mapv parse-line (str/split s #"\n"))
        registers (into {} (map (fn [n] {n 0}) (distinct (filter identity (map :reg program)))))]
    [program registers]))

(defn goal [program idx registers]
  ;; are we at a rcv with a register with a value
  (let [statement (get program idx)
        cmd (:cmd statement)]
    (if (= cmd :rcv)
      (let [reg (keyword (:val statement))
            val (reg registers)]
        (> val 0)))))

(defn play-sound [registers v]
  ;; (println "Play sound " v)
  v)

(defn set-value [registers reg val]
  (if (not (keyword? val))
    (assoc registers reg val)
    (assoc registers reg (val registers))))

(defn multi-value [registers reg val]

  (if (not (keyword? val))
    (assoc registers reg (* (reg registers) val))
    (assoc registers reg (* (reg registers) (val registers)))))

(defn mod-value [registers reg val]


  (if (not (keyword? val))
    (assoc registers reg (mod (reg registers) val))
    (assoc registers reg (mod (reg registers) (val registers)))))

(defn add-value [registers reg val]
  (assoc registers reg (+ (reg registers) val)))

(defn jump [registers reg val idx]

  (let [reg-value (reg registers)]

    (if (> reg-value 0)
      (+ idx val)
      (inc idx))))

(defn rcv-value [registers v]
  registers)

(defn step [program registers idx snd]
  ;; run the command at idx in program and return the updated regisers set and the next idx value
  (let [statement (get program idx)
        cmd (:cmd statement)]
    (let [[registers idx snd] (cond
                                (= cmd :set) [(set-value registers (:reg statement) (:val statement))      (inc idx)                                               snd]
                                (= cmd :mul) [(multi-value registers (:reg statement) (:val statement))    (inc idx)                                               snd]
                                (= cmd :jgz) [registers                                                    (jump registers (:reg statement) (:val statement) idx)  snd]
                                (= cmd :add) [(add-value registers (:reg statement) (:val statement))      (inc idx)                                               snd]
                                (= cmd :mod) [(mod-value registers (:reg statement) (:val statement))      (inc idx)                                               snd]
                                (= cmd :snd) [registers                                                    (inc idx)                                               (play-sound registers ((keyword (:val statement)) registers))]
                                (= cmd :rcv) [(rcv-value registers ((keyword (:val statement)) registers)) (inc idx)                                               snd]
                                )]
      [registers
       idx
       snd])))

(defn run [[program registers]]
  ;; instruction-pointer == index into program
  ;; registers[] == array of registers with names holding values
  (loop [[registers idx snd] [registers 0 0]]
    (if (goal program idx registers)
      ;; ((keyword (:val (get program idx))) registers)
      snd
      ;; process command at idx
      ;; exit if goal of rcv is called on a register with a value
      (recur (step program registers idx snd)))))

;; What is the value of the recovered frequency (the value of the most recently played sound) the first time a rcv instruction is executed with a non-zero value?

;; Part 1
(defn run-part1-test []
  (run (load-program test-program-text)))
;; 4

(defn run-part1 []
  (run (load-program program-text)))
;; 3423

;; Test case to ensure things work when refactoring
(defn part1-test []
  (and (= (run-part1-test) 4)
       (= (run-part1) 3423)))

