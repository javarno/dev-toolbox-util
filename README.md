<!--
  - MIT License
  -
  - Copyright © 2020 dev-toolbox.org
  -
  - Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
  - (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
  - distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
  - following conditions:
  -
  - The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
  -
  - THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
  - MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
  - CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
  - OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  -->

# dev-toolbox-util

Parent pom for dev-toolbox.org java utility projects.

- util-clean : Utility class to easily clean-up resources.
- util-dimension : Mutable and immutable implementations of simple dimension objects with width and height.
- util-exception : Runtime and checked implementations of parametric exceptions.
- util-filesystem : Utility class for nio files handling.
- util-http : Utility classes for http requests
- util-id : Interface and default implementation for identifiable objects using String.
- util-mime : File mime type detection using apache Tika.
- util-model : Listeners and events for data model modification system.
- util-resource : Utility class to handle classpath resources.
- util-task : Task sytem with listeners, status change, messages. Both synchronous and asynchronous implementations are available.
- util-thread : Implementation to execute a task at regular interval.

history
-------
- v1.8.0 2021/10/06 : jdk11 branch, downgraded slf4j-api to v1.7.32, logback to v1.2.6 / upgraded junit to v 5.8.1
- v1.7.4 2021/05/13 : upgrade all util projects to use the same version of dev-toolbox-util
- v1.7.3 2020/12/04 : upgrade javafx to 14.0.2.1, junit-jupiter to 5.7.0, surefire and failsafe plugins to 3.0.0-M5, moved util-exception and util-task to their own repository
- v1.7.2 2020/04/29 : define surefire and failsafe plugins version and override asm version for java 14 compatibility
- v1.7.1 2020/04/21 : fix version parameter name
- v1.7.0 2020/03/18 : java 14 and upgrade maven javadoc plugin to 3.2.0
- previous versions : history lost :)