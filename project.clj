(defproject gov.nasa.earthdata/cmr-service-bridge-docs "0.1.0-SNAPSHOT"
  :description "REST API and Source Code Documentation for CMR Service-Bridge"
  :url "https://github.com/cmr-exchange/cmr-service-bridge-docs"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [cheshire "5.8.1"]
    [http-kit "2.3.0"]
    [markdown-clj "1.0.3"]
    [org.clojure/clojure "1.9.0"]
    [selmer "1.12.2"]]
  :profiles {
    :ubercompile {
      :aot :all
      :source-paths ["test"]}
    :security {
      :plugins [
        [lein-nvd "0.5.5"]]
      :source-paths ^:replace ["src"]
      :nvd {
        :suppression-file "resources/security/false-positives.xml"}
      :exclusions [
        ;; The following are excluded due to their being flagged as a CVE
        [com.google.protobuf/protobuf-java]
        [com.google.javascript/closure-compiler-unshaded]
        ;; The following is excluded because it stomps on twig's logger
        [org.slf4j/slf4j-simple]]}
    :docs {
      :dependencies [
        [clojusc/trifl "0.4.0"]
        [clojusc/twig "0.4.0"]
        [gov.nasa.earthdata/codox-theme "1.0.0-SNAPSHOT"]]
      :plugins [
        [lein-codox "0.10.5"]
        [lein-marginalia "0.9.1"]]
      :source-paths ["resources/docs/src"]
      :codox {
        :project {
          :name "CMR Service-Bridge"
          :description "REST API and Source Code Documentation for CMR Service-Bridge"}
        :namespaces [#"^cmr\..*"]
        :metadata {
          :doc/format :markdown
          :doc "Documentation forthcoming"}
        :themes [:eosdis]
        :html {
          :transforms [[:head]
                       [:append
                         [:script {
                           :src "https://cdn.earthdata.nasa.gov/tophat2/tophat2.js"
                           :id "earthdata-tophat-script"
                           :data-show-fbm "true"
                           :data-show-status "true"
                           :data-status-api-url "https://status.earthdata.nasa.gov/api/v1/notifications"
                           :data-status-polling-interval "10"}]]
                       [:body]
                       [:prepend
                         [:div {:id "earthdata-tophat2"
                                :style "height: 32px;"}]]
                       [:body]
                       [:append
                         [:script {
                           :src "https://fbm.earthdata.nasa.gov/for/CMR/feedback.js"
                           :type "text/javascript"}]]]}
        :doc-paths ["resources/docs/markdown"]
        :output-path "resources/public/docs/service-bridge/docs/current/reference"}}
      :slate {
        :plugins [[lein-shell "0.5.0"]]}}
  :aliases {
    ;; Dev & Testing Aliases
    "ubercompile" ["with-profile" "+system,+geo,+local,+security" "compile"]
    "check-vers" ["with-profile" "+lint,+system,+geo,+security" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    ;; Security
    "check-sec" ["with-profile" "+system,+geo,+local,+security" "do"
      ["clean"]
      ["nvd" "check"]]
    ;; Documentation and static content
    "codox" ["with-profile" "+docs,+system,+geo" "codox"]
    "marginalia" ["with-profile" "+docs,+system,+geo"
      "marg" "--dir" "resources/public/docs/service-bridge/docs/current/marginalia"
             "--file" "index.html"
             "--name" "CMR Service-Bridge"]
    "slate" ["with-profile" "+slate"
      "shell" "resources/scripts/build-slate-docs"]
    "docs" ["do"
      ["codox"]
      ["marginalia"]
      ["slate"]]
    ;; Build tasks
    "build-jar" ["with-profile" "+security" "jar"]
    "build-uberjar" ["with-profile" "+security" "uberjar"]
    "build" ["do"
      ["clean"]
      ["check-vers"]
      ["check-sec"]
      ["ubercompile"]
      ["build-uberjar"]
      ["docs"]]
    ;; Publishing
    "publish" ["with-profile" "+system,+security,+geo" "do"
      ["clean"]
      ["build-jar"]
      ["deploy" "clojars"]]})
