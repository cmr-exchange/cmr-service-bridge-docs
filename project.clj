(defproject gov.nasa.earthdata/cmr-service-bridge-docs "0.1.0-SNAPSHOT"
  :description "REST API and Source Code Documentation for CMR Service-Bridge"
  :url "https://github.com/cmr-exchange/cmr-service-bridge-docs"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [cheshire "5.8.1"]
    [clojusc/system-manager "0.3.0-SNAPSHOT"]
    [clojusc/trifl "0.4.0"]
    [clojusc/twig "0.4.0"]
    [com.esri.geometry/esri-geometry-api "2.2.1"]
    [com.stuartsierra/component "0.3.2"]
    [com.vividsolutions/jts "1.13"]
    [environ "1.1.0"]
    [gov.nasa.earthdata/cmr-authz "0.1.1-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-exchange-common "0.2.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-exchange-query "0.2.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-http-kit "0.1.3-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-jar-plugin "0.1.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-metadata-proxy "0.1.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-mission-control "0.1.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-ous-plugin "0.2.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-site-templates "0.1.0-SNAPSHOT"]
    [gov.nasa.earthdata/cmr-sizing-plugin "0.1.0-SNAPSHOT"]
    [http-kit "2.3.0"]
    [markdown-clj "1.0.4"]
    [me.raynes/conch "0.8.0"]
    [metosin/reitit-core "0.2.3"]
    [metosin/reitit-ring "0.2.3"]
    [metosin/ring-http-response "0.9.0"]
    [net.sf.geographiclib/GeographicLib-Java "1.49"]
    [org.clojure/clojure "1.9.0"]
    [org.clojure/core.async "0.4.474"]
    [org.clojure/core.cache "0.7.1"]
    [org.clojure/data.xml "0.2.0-alpha5"]
    [org.clojure/java.classpath "0.3.0"]
    [org.clojure/tools.namespace "0.2.11"]
    [org.geotools/gt-geometry "20.0"]
    [org.geotools/gt-referencing "20.0"]
    [ring/ring-codec "1.1.1"]
    [ring/ring-core "1.7.0"]
    [ring/ring-defaults "0.3.2"]
    [selmer "1.12.2"]
    [tolitius/xml-in "0.1.0"]]
  :plugins [
    [lein-shell "0.5.0"]]
  :repositories [
    ["osgeo" "https://download.osgeo.org/webdav/geotools"]]
  :source-paths ["repos/sources"]
  :profiles {
    :ubercompile {
      :aot :all}
    :security {
      :plugins [
        [lein-nvd "0.5.5"]]
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
      :codox {
        :project {
          :name "CMR Service-Bridge"
          :description "REST API and Source Code Documentation for CMR Service-Bridge"}
        :namespaces [#"^cmr\..*"]
        :metadata {
          :doc/format :markdown
          :doc "Documentation forthcoming"}
        :themes [:eosdis]
        :exclude-vars #"^((map)?->)?\p{Upper}"
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
        :output-path "resources/public/docs/service-bridge/docs/current/reference"}}}
  :aliases {
    ;; Dev & Testing Aliases
    "ubercompile" ["with-profile" "+ubercompile,+security" "compile"]
    "check-vers" ["with-profile" "+lint,+system,+security" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    ;; Security
    "check-sec" ["with-profile" "+system,+security" "do"
      ["clean"]
      ["nvd" "check"]]
    ;; Documentation and static content
    "prep-sources" ["shell" "resources/scripts/prep-sources"]
    "cleanup-sources" ["shell" "resources/scripts/cleanup-sources"]
    "codox" ["with-profile" "+docs,+system" "codox"]
    "marginalia" ["with-profile" "+docs,+system"
      "marg" "--dir" "resources/public/docs/service-bridge/docs/current/marginalia"
             "--file" "index.html"
             "--name" "CMR Service-Bridge"]
    "slate" ["shell" "resources/scripts/build-slate-docs"]
    "docs" ["do"
      ["prep-sources"]
      ["codox"]
      ["marginalia"]
      ["slate"]
      ["cleanup-sources"]]
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
    "publish" ["with-profile" "+system,+security" "do"
      ["clean"]
      ["build-jar"]
      ["deploy" "clojars"]]})
