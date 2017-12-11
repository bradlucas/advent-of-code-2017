(ns advent.day11.hexed)

;; Think along x y graph
;; Notice ne you go plus 1 in both x and y directions
;; s is -1 in the y
;; n is +1 along the y
;; There are no e and w


(def url "./src/advent/day11/day11.txt")


(defn process-str [s]
  (map keyword (clojure.string/split s '#",")))

(defn load-data [url]
  (clojure.string/trim (slurp url)))
  
(defn command [c]
  (case c
    :n [0 1]
    :ne [1 1]
    :se [1 -1]
    :s [0 -1]
    :sw [-1 -1]
    :nw [-1 1]
    nil))
  
(defn solve [s]
  (reduce
   (fn [[a b] c]
     (let [[c d] (command c)]
       [(+ a c) (+ b d)]
       ))
   [0 0]
   (process-str s)))
;; [696 -270]


;; ne,ne,ne is 3 steps away.
;; ne,ne,sw,sw is 0 steps away (back where you started).
;; ne,ne,s,s is 2 steps away (se,se).
;; se,sw,se,sw,sw is 3 steps away (s,s,sw).

;; Calc shortest path
;; [X Y]
;;
;; Find diagnoal. Less of x and y
;; [x y]
;;
;; The rest is along x y so 1/2 it
;;

(defn abs [n] (max n (- n)))

(defn abs-both [x y]
  [(abs x)
   (abs y)]
  )

(defn smaller-larger [x y]
  (if (< x y)
    [x y]
    [y x]))

(defn calc-path [[x y]]
  ;; abs both values
  (let [[a b] (abs-both x y)]
    (if (= a b)
      a
      ;; smallest value
      ;; diff with larger divided by 2
      (let [[s l] (smaller-larger a b)]
        (if (= s 0)
          l
          (let [diff (- l s)]
            (println diff l s)
            (abs (+ s (/ diff 2)))))))))

;; (defn calc-path [[x y]]
;;   ;; (abs(x) + abs(y) + abs(x+y)) / 2
;;   (/ (max (abs x) (abs y) (abs (+ x y))) 2))

;; (defn calc-path [[x y]]
;;   (Math/ceil (/ (+ (abs x) (abs y)) 2)))

(defn calc-path [[x y]]
  (let [[a b] (abs-both x y)]
    (if (= a b)
      a
      (if (or (= a 0) (= b 0))
        (max a b)
        (+ a (abs (int (/ b 2))))))))

;; (defn calc-path [[x y]]
;;   (let [[dy dx] (map abs [x y])]
;;     (Math/round (double (+ dx (max (- dy (double (/ dx 2))) 0))))))


(defn run-test [s, v]
  (println "Running test on" s "with expected results of" v)
  (let [rtn (solve s)]
    (println  rtn (calc-path rtn))))
  
(defn run-tests []
  ;; ne,ne,ne is 3 steps away.
  ;; ne,ne,sw,sw is 0 steps away (back where you started).
  ;; ne,ne,s,s is 2 steps away (se,se).
  ;; se,sw,se,sw,sw is 3 steps away (s,s,sw).
  
  (run-test "ne,ne,ne", 3)       ;; is 3 steps away.
  (run-test "ne,ne,sw,sw", 0)    ;; is 0 steps away (back where you started).
  (run-test "ne,ne,s,s", 2)      ;; is 2 steps away (se,se).
  (run-test "se,sw,se,sw,sw", 3) ;; is 3 steps away (s,s,sw).
)


(defn run-part1 []
  (let [rtn (solve (load-data url))]
    (println rtn)
    (println (calc-path rtn))))

;; [696 -227]
;; 696 Is correct

;; 213 (too low)



;; Part 2
;; What is the max distance ever reached

(defn find-max [a b m]
  (max (abs m) (abs a) (abs b)))


(defn solve2 [s]
  (reduce
   (fn [[a b m] c]
     (let [[c d] (command c)
           a' (+ a c)
           b' (+ b d)
           m' (find-max a' b' m)]
       [a' b' m']
       ))
   [0 0 0]
   (process-str s)))

(defn run-part2 []
  (let [rtn (solve2 (load-data url))]
    (nth rtn 2)))
;; 1461
 
