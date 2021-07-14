(ns chess.stockfish
  (:require [chess.common :as c]
            [clojure.java.shell :as shell]
            [clojure.string :as s]))

(defn extract-last-line
  "The last line can be one of:
    bestmove <move>
    bestmove <move> ponder <other-move>"
  [last-line]
  (-> last-line
      (s/split #" ")
      second))

(defn best-move
  [fen & {:keys [move-time] :or {move-time 3000}}]
  (let [fen-command  (str "position fen " fen)
        move-command (str "go movetime " move-time)
        padding      (apply str (take 500 (repeat "\n"))) ; nasty hack to stop the stockfish process from closing too early
        input        (str fen-command "\nd\n" move-command padding)]
    (some-> (shell/sh "stockfish" :in input)
            :out
            s/split-lines
            last
            extract-last-line)))

(defn apply-uci
  [fen uci]
  (let [input (str "position fen " fen " moves " uci "\nd\n")]
    (some-> (shell/sh "stockfish" :in input)
            :out
            s/split-lines
            (#(filter (fn [l] (re-matches #"^Fen:.*" l)) %))
            first
            (subs 5))))
