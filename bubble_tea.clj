; Gzmask's bubble tea zeph Windows Manager
; ~/.lein/profiles.clj: {:user {:plugins [[lein-exec "0.3.0"]]}}
; lein exec ~/.zeph/bubble_tea.clj

(use '[leiningen.exec :only (deps)])
;(use '[clojure.set :only (union)])
(deps '[[org.clojure/data.json "0.2.2"]])

(load-file "/Applications/Zephyros.app/Contents/Resources/libs/zephyros.clj")

(defn split2w [frame] 
  [{:x (:x frame)
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (:h frame)}
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (:h frame)}])

(defn split2e [frame] [(second (split2w frame)) (first (split2w frame))])

(defn split2n [frame]
  [{:x (:x frame)
    :y (:y frame)
    :w (:w frame)
    :h (/ (:h frame) 2)}
   {:x (:x frame)
    :y (+ (:y frame) (/ (:h frame) 2))
    :w (:w frame)
    :h (/ (:h frame) 2)}])

(defn split2s [frame] [(second (split2n frame)) (first (split2n frame))])

(defn split3w [frame] 
  [(first (split2w frame))
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (+ (:y frame) (/ (:h frame) 2))
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}])

(defn split3e [frame] 
  [(first (split2e frame))
   {:x (:x frame)
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}
   {:x (:x frame)
    :y (+ (:y frame) (/ (:h frame) 2))
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}])

(defn split3n [frame] 
  [(first (split2n frame))
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (+ (:y frame) (/ (:h frame) 2))
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}
   {:x (:x frame)
    :y (+ (:y frame) (/ (:h frame) 2))
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}])

(defn split3s [frame] 
  [(first (split2s frame))
   {:x (:x frame)
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}
   {:x (+ (:x frame) (/ (:w frame) 2))
    :y (:y frame)
    :w (/ (:w frame) 2)
    :h (/ (:h frame) 2)}])

(defn disj_windows [stack fwin]
  (disj (into #{} (for [win stack]
    (if (not= (get-top-left win) (get-top-left fwin))
      win))) nil))

(defn bubble-west [fwin stack frame]
  (let [stack_ (if fwin (disj_windows stack fwin) stack)
        stackv (if fwin (into [fwin] stack_) (into [] stack_))]
    (cond
      (= (count stackv) 1) (set-frame (first stackv) frame)
      (= (count stackv) 2) (do 
                             (set-frame (first stackv) (first (split2w frame))) 
                             (set-frame (second stackv) (second (split2w frame))))
      (= (count stackv) 3) (do 
                             (set-frame (first stackv) (first (split3w frame))) 
                             (set-frame (second stackv) (second (split3w frame)))
                             (set-frame (last stackv) (last (split3w frame))))
      :else (do 
              (set-frame (first stackv) (first (split3w frame))) 
              (set-frame (second stackv) (second (split3w frame)))
              (bubble-west
                nil
                (into #{} (rest (rest stackv)))
                (last (split3w frame))
              )))))

(defn bubble-east [fwin stack frame]
  (let [stack_ (if fwin (disj_windows stack fwin) stack)
        stackv (if fwin (into [fwin] stack_) (into [] stack_))]
    (cond
      (= (count stackv) 1) (set-frame (first stackv) frame)
      (= (count stackv) 2) (do 
                             (set-frame (first stackv) (first (split2e frame))) 
                             (set-frame (second stackv) (second (split2e frame))))
      (= (count stackv) 3) (do 
                             (set-frame (first stackv) (first (split3e frame))) 
                             (set-frame (second stackv) (second (split3e frame)))
                             (set-frame (last stackv) (last (split3e frame))))
      :else (do 
              (set-frame (first stackv) (first (split3e frame))) 
              (set-frame (second stackv) (second (split3e frame)))
              (bubble-east
                nil
                (into #{} (rest (rest stackv)))
                (last (split3e frame))
              )))))

(defn bubble-north [fwin stack frame]
  (let [stack_ (if fwin (disj_windows stack fwin) stack)
        stackv (if fwin (into [fwin] stack_) (into [] stack_))]
    (cond
      (= (count stackv) 1) (set-frame (first stackv) frame)
      (= (count stackv) 2) (do 
                             (set-frame (first stackv) (first (split2n frame))) 
                             (set-frame (second stackv) (second (split2n frame))))
      (= (count stackv) 3) (do 
                             (set-frame (first stackv) (first (split3n frame))) 
                             (set-frame (second stackv) (second (split3n frame)))
                             (set-frame (last stackv) (last (split3n frame))))
      :else (do 
              (set-frame (first stackv) (first (split3n frame))) 
              (set-frame (second stackv) (second (split3n frame)))
              (bubble-north
                nil
                (into #{} (rest (rest stackv)))
                (last (split3n frame))
              )))))

(defn bubble-south [fwin stack frame]
  (let [stack_ (if fwin (disj_windows stack fwin) stack)
        stackv (if fwin (into [fwin] stack_) (into [] stack_))]
    (cond
      (= (count stackv) 1) (set-frame (first stackv) frame)
      (= (count stackv) 2) (do 
                             (set-frame (first stackv) (first (split2s frame))) 
                             (set-frame (second stackv) (second (split2s frame))))
      (= (count stackv) 3) (do 
                             (set-frame (first stackv) (first (split3s frame))) 
                             (set-frame (second stackv) (second (split3s frame)))
                             (set-frame (last stackv) (last (split3s frame))))
      :else (do 
              (set-frame (first stackv) (first (split3s frame))) 
              (set-frame (second stackv) (second (split3s frame)))
              (bubble-south
                nil
                (into #{} (rest (rest stackv)))
                (last (split3s frame))
              )))))

(bind "h" ["Cmd" "Shift"] (fn [] 
                    (let [stack (into #{} (get-visible-windows)) 
                          window (get-focused-window) 
                          screen (get-screen-for-window window)
                          frame (screen-frame-without-dock-or-menu screen)]
                    (bubble-west window stack frame))))

(bind "l" ["Cmd" "Shift"] (fn [] 
                    (let [stack (into #{} (get-visible-windows)) 
                          window (get-focused-window) 
                          screen (get-screen-for-window window)
                          frame (screen-frame-without-dock-or-menu screen)]
                    (bubble-east window stack frame))))

(bind "j" ["Cmd" "Shift"] (fn [] 
                    (let [stack (into #{} (get-visible-windows)) 
                          window (get-focused-window) 
                          screen (get-screen-for-window window)
                          frame (screen-frame-without-dock-or-menu screen)]
                    (bubble-south window stack frame))))

(bind "k" ["Cmd" "Shift"] (fn [] 
                    (let [stack (into #{} (get-visible-windows)) 
                          window (get-focused-window) 
                          screen (get-screen-for-window window)
                          frame (screen-frame-without-dock-or-menu screen)]
                    (bubble-north window stack frame))))

(bind "h" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-left window))))

(bind "l" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-right window))))

(bind "k" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-up window))))

(bind "j" ["Cmd"]
          (fn [] 
            (let [window (get-focused-window)] 
              (focus-window-down window))))

(bind "r" ["Cmd" "Shift"] (fn [] 
                    (let [stack (into #{} (get-visible-windows)) 
                          window (get-focused-window) 
                          screen (get-screen-for-window window)
                          frame (screen-frame-without-dock-or-menu screen)]
                    (relaunch-config))))

@listen-for-callbacks ;; necessary when you use (bind) or (listen)
