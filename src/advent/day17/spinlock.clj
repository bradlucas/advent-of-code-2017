(ns advent.day17.spinlock)


(defn cycle [m pos iter]
  ;; starting at pos move forward circularly iter number of steps
  ;; return the now current position
  (let [len (count m)
        nxt (mod (+ pos iter) len)]
    nxt))

(defn insert [m pos cnt]
  ;; insert cnt inbetween the pos position and the next position
  ;; return the new m and the new position
  (let [[l r] (map vec (split-at (inc pos) m))
        m1 (vec (flatten (conj l cnt r)))]
    [m1 (mod (inc pos) (count m1))]))

(defn spin [m pos iter cnt]
  ;; move through m iter amount start at idx position
  ;; insert cnt after the final position
  (let [pos1 (cycle m pos iter)]
    (insert m pos1 cnt)))
 
(defn run [iter num]
  (loop [[m pos] [[0] 0]
         cnt 0]
    (if (>= cnt num)
      m
      (recur (spin m pos iter (inc cnt)) (inc cnt)))
    ))


(defn run-part1-test []
  (let [m (run 3 2017)]
    (get m (inc (.indexOf m 2017)))))


(defn run-part1 []
  (let [m (run 355 2017)]
    (get m (inc (.indexOf m 2017)))))
;; 1912

;; Part 2
;; Run fifty million cycles
;; Get the value after 0

;; Doesn't return
;; (defn run-part2 []
;;   (let [m (run 355 50000000)]
;;     (get m (inc (.indexOf m 0)))))

;; Track the second position
;; @see https://github.com/vvvvalvalval/advent-of-code-2017/blob/master/src/aoc2017/day17.clj
(defn run-part2 []
  (let [input 355]
    (loop [pos1 0
           n 1
           pos 0]
      (if (= n 50000000)
        pos1
        (let [v n
              i (-> pos (+ input) (mod n))
              next-pos (inc i)
              next-pos1 (if (= i 0) v pos1)]
          (recur next-pos1 (inc n) next-pos))))))
;; 21066990
         
