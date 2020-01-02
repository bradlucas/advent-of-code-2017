(ns advent.day09.stream)


;; { .. } == group , groups are nestable  { { .. } }
;;
;; < .. > == garbage
;;
;; !x == ignore x
;;

(def url "./src/advent/day09/day09.txt")

(defn load-data [url]
  (slurp url))



;; Remove all !x pairs
;;
(defn remove-comments [s]
  ;; remove !x pairs
  (loop [s s
         r []]
    (if (empty? s)
      r
      ;;
      (let [a (first s)
            b (second s)]
        (let [s2 (if (= \! a) (next (next s)) (next s))
              r2 (if (= \! a) r (conj r a))]
          (recur s2 r2))))))
  
  

;; Remove garbage
;;
(defn garbage-fn [s r g c]
  (if g
    (if (= \> c)
      [(next s) r false]
      [(next s) r g])
    (if (= \< c)
      [(next s) r true]
      [(next s) (conj r c) false])))
    
(defn remove-garbage [s]
  ;; remove <....>
  (loop [s s
         r []
         in-garbage false]
    (if (empty? s)
      r
      (let [c (first s)
            [s1 r1 in-garbage1] (garbage-fn s r in-garbage c)]
        (recur s1 r1 in-garbage1)))))

(defn score-function [c cnt stack]
  ;; Push each { and add it's value based on level of stack
  ;; Pop each { when } is found
  ;; (println c cnt stack)
  (let [stack-height (count stack)]
    (if (= \{ c)
      [(conj stack c) (+ cnt stack-height 1)]
      (if (= \} c)
        [(pop stack) cnt]
        [stack cnt]))))
  
(defn calc-group-scores [s]
  (loop [s s
         cnt 0
         stack []]
    (if (empty? s)
      cnt
      (let [c (first s)
            [stack1 cnt1] (score-function c cnt stack)]
        (recur (next s) cnt1 stack1)))))

         
  


;; {{{{{{<}>,{<<'!!i}!>!!!!!>},<{,'!>,<!!e
;;

;; <!!!>> => <>

;; Part 1
;;
(defn run-part1 []
  (let [v (load-data url)]
    (calc-group-scores (remove-garbage (remove-comments v)))))
;; 12505



;; Part 2

;; Count the characters with the garbage

(defn garbage-character-count-fn [s r g c cnt]
  (if g
    (if (= \> c)
      [(next s) r false cnt]
      [(next s) r g (inc cnt)])
    (if (= \< c)
      [(next s) r true cnt]
      [(next s) (conj r c) false cnt])))
    
(defn count-characters-in-garbage [s]
  ;; remove <....>
  (loop [s s
         r []
         in-garbage false
         cnt 0]
    (if (empty? s)
      cnt
      (let [c (first s)
            [s1 r1 in-garbage1 cnt] (garbage-character-count-fn s r in-garbage c cnt)]
        (recur s1 r1 in-garbage1 cnt)))))



(defn run-part2 []
  (let [v (load-data url)]
    (count-characters-in-garbage (remove-comments v))))
;; 6671
