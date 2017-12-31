(ns advent.day18.duet3
  (:refer-clojure :exclude [set mod])
  (:require [clojure.string :as str]
            [clojure.core.async :as async]
            [advent.day18.duet :as duet]))
;; @see https://github.com/kixi/advent-of-code-2017/blob/master/src/advent_of_code/day18_2.clj

;; Use queues to 'snd' and 'rcv' values between named programs 0 and 1
;; Track number of times program 1 sends a value
;; End both programs when they deadlock trying to 'rcv' on empty queues

(defn getval [env param]
  (if (or (symbol? param) (keyword? param))
    (env param 0)
    param))

(defn set [env param1 param2]
  (assoc env param1 (getval env param2)))

(defn add [env param1 param2]
  (update env param1 (fnil + 0) (getval env param2)))

(defn mul [env param1 param2]
  (update env param1 (fnil * 0) (getval env param2)))

(defn mod [env param1 param2]
  (update env param1 (fnil rem 0) (getval env param2)))

(defn snd [env param1]
  #_(println (:prog-id env) ": Send message " (getval env param1))
  (async/>!! (:out env) (getval env param1))
  (update env :send-msgs (fnil inc 0)))

(defn rcv [env param1]
  (let [[v ch](async/alts!! [(:in env)
                             (async/timeout 100)])]
    (if v
      (do
        #_(println (:prog-id env) ": Message received " v)
        (assoc env param1 v))
      (do
        ;; (println (:prog-id env) ": Timeout ")
        (assoc env :timeout true)))))

(defn jgz [env param1 param2]
  (if (> (getval env param1) 0)
    (assoc env :ip (dec (+ (getval env :ip) (getval env param2))))
    env))

(defn step [[cmd reg val] env]
  #_(println (format "%s %s %s" cmd reg val))
  (condp = cmd
    'set (set env reg val)
    'add (add env reg val)
    'mul (mul env reg val)
    'mod (mod env reg val)
    'snd (snd env reg)
    'rcv (rcv env reg)
    'jgz (jgz env reg val)
    (println "Unknown comand")))

(defn run [program prog-id send-queue receive-queue]
  (loop [env {:ip 0
              'p prog-id
              :in receive-queue
              :out send-queue
              :prog-id prog-id
              :program program
              }]
    ;; (println "\n\nProgram" program-id " --- \n" env "\n\n")
    (if (and (not (:timeout env)) (< (:ip env) (count program)))
      (recur (step (get program (:ip env)) (update env :ip inc)))
      ;; Print the numbrer of messages sent by Program 1
      (do
        (if (= 1 (:prog-id env)) (println (:send-msgs env)))))))

(defn run-ex [program]
  (let [queue1 (async/chan 10000)
        queue2 (async/chan 10000)]
    [(async/thread (run program 0 queue1 queue2))
     (async/thread (run program 1 queue2 queue1))]))

(defn load-program [txt]
  (->> txt
       str/split-lines
       (map #(str "[" % "]"))
       (mapv read-string)))

(defn run-part2 []
  (run-ex (load-program duet/program-text)))
;; 7493
