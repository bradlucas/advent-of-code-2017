(ns advent.day18.duet2
  (:require [clojure.string :as str]
            [clojure.core.async :as async]
            [advent.day18.duet :as duet]))

(def test-string "snd 1\nsnd 2\nsnd p\nrcv a\nrcv b\nrcv c\nrcv d")

(defn snd-value [num-name registers val queue snd]
  (let [v (if (not (keyword? val)) val (val registers))]
    ;; (println (format "snd-value program %d value %d snd-count %d" num-name v snd))
    (async/>!! queue v)
    (inc snd)))

(defn rcv-value [num-name registers reg queue snd]
  ;; (println "rcv-value" num-name)
  (let [[v ch] (async/alts!! [queue (async/timeout 10000)])]
    (if v
      (do
        ;; (println (format "rcv-value for program %d received %d" num-name v))
        (duet/set-value registers reg v))
      (do
        (println  (format "TIMEOUT: rcv-value for program %d value of snd is %d : %s" num-name snd registers))
        nil))))

(defn step2 [num-name program registers idx snd receive-queue send-queue]
  (let [statement (get program idx)
        cmd (:cmd statement)]
    ;; (println num-name statement)
    (condp = cmd
      :snd [registers
            (inc idx)
            (snd-value num-name registers (:val statement) send-queue snd)]
      :rcv [(rcv-value num-name registers (:val statement) receive-queue snd)
            (inc idx)
            snd]
      (duet/step program registers idx snd))
    ))

(defn run [num-name program registers send-queue receive-queue]
  (loop [[registers idx snd] [(assoc registers :p num-name) 0 0]]
    (if registers
      (recur (step2 num-name program registers idx snd send-queue receive-queue))
      (do
        (println snd)
        snd))))

;; Use queues to 'snd' and 'rcv' values between named programs 0 and 1
;; Track number of times program 1 sends a value
;; End both programs when they deadlock trying to 'rcv' on empty queues

(defn run-ex [[program registers]]
  (let [queue1 (async/chan 10000)
        queue2 (async/chan 10000)]
    [(async/thread (run 0 program registers queue1 queue2))
     (async/thread (run 1 program registers queue2 queue1))]))

(defn run-part2 []
  ;; (println "run-part2")
  (run-ex (duet/load-program duet/program-text)))

;; Correct Answer
;; 7493
