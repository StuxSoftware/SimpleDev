SimpleDev
=========

Library to make platform independent plugins (and mods) easier.

Building
--------
Build this package using maven:
```mvn clean install```

License
-------
This library is licensed under the "Apache License 2.0". The license file is
shipped under the "LICENSE"-File.

You are explicitly allowed to shade this library into your source code
as long as you are mentioning the library according to the
"Apache License 2.0".

This library requires "SnakeYaml" and "Apache Commons Lang" which are
licensed under the "Apache License 2.0", too. The license file is shipped
in the file "LICENSE"

Even when "Bukkit" is licensed under the GPLv3, the Bukkit Development Team
has announced that they won't enforce it's license for its plugins. (1)

This library can be used with CanaryLib licensed under the BSD 3-Clause
 License.
 
This library can be used with BungeeCord licensed under a modified BSD-4-Clause
 License with an additional clause in the license.

(1) Source: https://forums.bukkit.org/threads/what-license-is-the-bukkit-project-under.154/

Features
--------

 * Command API - Implement Commands Platform Independent.
 * Configuration API - Implement Configurations without worrying that the Plugin-API cannot
   handle these plugins.

Support
-------
This libarry currently supports Bukkit, CanaryMod Recode and BungeeCord.
