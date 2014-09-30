(function(){
    var hostWindow = window.parent, registered = false;

    function importPackage(pkgs, options) {
        if (!registered) {
            hostWindow.cloudo.plugins.registerCloudoPackagesConfig(window);
            registered = true;
        }
        $import(pkgs, options);
    }

    window.importCloudoPackage = importPackage;
    window.registerCloudoHostWindow = function(w) {
        hostWindow = w;
    };
})();