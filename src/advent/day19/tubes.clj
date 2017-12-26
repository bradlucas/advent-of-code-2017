(ns advent.day19.tubes
  (:require [clojure.string :as str]))


 ;;     |          
 ;;     |  +--+    
 ;;     A  |  C    
 ;; F---|----E|--+ 
 ;;     |  |  |  D 
 ;;     +B-+  +--+ 

(def s "     |          \n     |  +--+    \n     A  |  C    \n F---|----E|--+ \n     |  |  |  D \n     +B-+  +--+ ")

(def input-file "src/advent/day19/day19.txt")

(defn load-diagram
  "Read the textual version of the diagram and return it as a two dimensional array"
  [s]
  (let [parse-line (fn [s] (vec (seq s)))]
    (mapv parse-line (str/split s #"\n"))))

;; direction = which direction are we currently headed in
;; next() = return next position
;; save letters to return later
;; pos = current position

;; Values: | + - A

(defn get-value [diag [row col]]
  (get (get diag row) col))

(defn get-starting-position [diag]
  ;; return the row and col of the \| entry point in the first row
  (let [row (get diag 0)]
    [0 (.indexOf row \|)]))

(defn get-row-count [diag]
  (count diag))

(defn get-col-count [diag]
  (count (first diag)))

(defn get-size [diag]
  [(get-row-count diag)
   (get-col-count diag)])
  
(defn letter? [c]
  ;; (println "letter?" c)
  (and (>= (int c) (int \A))
       (<= (int c) (int \Z))))

(defn get-next-pos [pos dir]
  ((fn [[a b] [c d]] [(+ a c) (+ b d)]) pos dir))

(defn offmap? [diag pos]
  (let [[row col] pos
        [height width] (get-size diag)]
    (if (or (> row height)
            (> col width))
      true
      false)))

(defn continue [diag pos dir path]
  ;; (println "continue" pos dir path)
  (let [c (get-value diag pos)
        path (if (letter? c) (conj path c) path)
        pos (get-next-pos pos dir)]
    (if (or (offmap? diag pos) (= (get-value diag pos) \space))
      [nil dir path]
      [pos dir path])))

(def up [-1 0])
(def down [1 0])
(def right [0 1])
(def left [0 -1])

(defn turn-left [dir]
  (cond
    (= dir up) left
    (= dir down) right
    (= dir left) down
    (= dir right) up))

(defn turn-right [dir]
  (cond
    (= dir up) right
    (= dir down) left
    (= dir left) up
    (= dir right) down))

(defn turn [diag pos dir path]
  ;; you can't continue in dir
  ;; you can't backup
  ;; you can turn left and right from where you are
  ;; which has a non-space?
  (let [dirl (turn-left dir)
        dirr (turn-right dir)
        vall (get-value diag (get-next-pos pos dirl))
        valr (get-value diag (get-next-pos pos dirr))
        ]
    (if (= vall \space)
      [(get-next-pos pos dirr) dirr path]
      [(get-next-pos pos dirl) dirl path])))
                 
(defn step [diag pos dir path]
  ;; return next pos, new direction and the stored letters
  ;; (println "step" pos dir path)
  (let [c (get-value diag pos)]
    ;; (println c)
    (if (or (= c \|)
             (= c \-)
             (letter? c))
      (continue diag pos dir path)
      ;; (= c \+)
      (turn diag pos dir path)
      )
    )
  )

(defn traverse [diag]
  (loop [[pos dir path] [(get-starting-position diag) down []]]
    (if (nil? pos)
      path
    (recur (step diag pos dir path))
    )
    ))

(defn run-part1 []
  (apply str (traverse (load-diagram (slurp input-file)))))
;; RYLONKEWB


(defn traverse2 [diag]
  (loop [[pos dir path] [(get-starting-position diag) down []]
         cnt 0]
    (if (nil? pos)
      [(apply str path) cnt]
    (recur (step diag pos dir path) (inc cnt))
    )
    ))


(defn run-part2-test []
  (let [[s cnt](traverse2 (load-diagram s))]
    ;; (println s)
    cnt))


(defn run-part2 []
  (let [[s cnt](traverse2 (load-diagram (slurp input-file)))]
    ;; (println s)
    cnt))
;; 16016
