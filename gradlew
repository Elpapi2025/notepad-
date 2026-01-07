#!/usr/bin/env sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# @author: Andres Almiray
#

#
# This script is an adaptation of an original script from the spring-boot project.
#

#
# Environment Variable Prequisites
#
#   JAVA_HOME   Home of Java installation.
#
#   GRADLE_OPTS Extra options to pass to the Gradle command.
#
#   GRADLE_HOME (Optional) Path to a Gradle distribution. If not set, this script will
#               use the `gradle-wrapper.jar` to download the distribution specified in

#               `gradle-wrapper.properties`.
#
# In addition, this script will use `jshell` if available.
# Otherwise it will use `java`.
#

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Determine the JShell command to use to start JShell.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/jshell" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JSHCMD="$JAVA_HOME/jre/sh/jshell"
    else
        JSHCMD="$JAVA_HOME/bin/jshell"
    fi
fi

# OSX hack to make `ps` work correctly
if [ "$(uname)" = "Darwin" ]; then
    ps_opts="-o pid,command"
else
    ps_opts="-o pid,args"
fi

#
# Helper functions
#

die () {
    echo "$*"
    exit 1
}

#
# Resolve links - $0 may be a softlink
#
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
SAVED_PRG="$PRG"

#
# Collect arguments
#
SAVED_ARGS="$@"

#
# For Cygwin, ensure paths are in UNIX format before anything is touched
#
if $cygwin ; then
    [ -n "$GRADLE_HOME" ] &&
        GRADLE_HOME=`cygpath --unix "$GRADLE_HOME"`
    [ -n "$JAVA_HOME" ] &&
        JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi

#
# Get script directory
#
PRG_DIR=`dirname "$PRG"`

#
# Get installation directory
#
if [ -z "$GRADLE_HOME" ]; then
    # resolved gradle-wrapper.jar, but with a fallback to the current directory
    GRADLE_WRAPPER_JAR_LOCATIONS=("${PRG_DIR}/gradle/wrapper/gradle-wrapper.jar" "${PRG_DIR}/gradle-wrapper.jar")
    for gradle_wrapper_jar_candidate in "${GRADLE_WRAPPER_JAR_LOCATIONS[@]}"; do
        if [ -f "${gradle_wrapper_jar_candidate}" ]; then
            GRADLE_WRAPPER_JAR="${gradle_wrapper_jar_candidate}"
            break
        fi
    done

    if [ -z "$GRADLE_WRAPPER_JAR" ]; then
        # Fallback to searching the directory of the script and all parent directories
        # This is useful when the script is linked from a centralised tools directory
        # e.g. /opt/tools/gradle -> /opt/gradle-5.4/bin/gradle
        CURRENT_DIR="`pwd`"
        while [ -n "${CURRENT_DIR}" ]; do
            # Use the absolute path to the wrapper jar, so that the script can be used from any directory
            potential_jar_path="${CURRENT_DIR}/gradle/wrapper/gradle-wrapper.jar"
            if [ -f "${potential_jar_path}" ]; then
                GRADLE_WRAPPER_JAR="${potential_jar_path}"
                break
            fi
            # if we have reached the root directory, stop
            if [ "${CURRENT_DIR}" = "/" ]; then
                break
            fi
            # Move up one directory
            CURRENT_DIR=`dirname "${CURRENT_DIR}"`
        done
    fi

    if [ -z "${GRADLE_WRAPPER_JAR}" ] ; then
        die "Could not locate gradle-wrapper.jar"
    fi
    exec "$JAVACMD" "${JVM_OPTS[@]}" -Dorg.gradle.appname="gradlew" -classpath "$GRADLE_WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
fi

#
# Set GRADLE_MAIN_CLASS
#
if [ -z "$GRADLE_MAIN_CLASS" ]; then
    GRADLE_MAIN_CLASS=org.gradle.launcher.GradleMain
fi
export GRADLE_MAIN_CLASS

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
APP_HOME="$GRADLE_HOME"
DEFAULT_JVM_OPTS=()

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
#
# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# Add application-specific classpath entries to this list.
CLASSPATH="$APP_HOME/lib/*.jar"

#
# Preserve real script path
#
PRG_PATH=`dirname "$SAVED_PRG"`
if [ -n "$PRG_PATH" ]; then
  PRG_PATH="`cd "$PRG_PATH" ; pwd`"
fi

#
# For Cygwin, switch paths to Windows format before running java
#
if $cygwin; then
    APP_HOME=`cygpath --path --windows "$APP_HOME"`
    CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

#
# Runs the given command in a new process, setting the current process
# to the new process id.
#
run_in_background() {
    (
        trap '' HUP
        "$@" &
    ) > /dev/null 2>&1
    __pid=$!
}

#
# Print a stack trace to stderr
#
print_stack_trace() {
    if [ -n "$JSHCMD" ] ; then
        "$JSHCMD" --startup DEFAULT \
                  --startup JSTACK_PRINT \
                  --var __pid=$1 \
                  -
    else
        jstack $1
    fi
} >&2

#
# Check whether a process is still running
#
is_running() {
    ps $ps_opts | grep "$1" | grep -v "grep" > /dev/null
}

#
# Stops the given process id
#
stop_process() {
    if is_running $1 ; then
        kill $1
        while is_running $1 ; do sleep 0.1; done
    fi
}

#
# Wait for the given process id to finish
#
wait_for_process() {
    if is_running $1 ; then
        while is_running $1 ; do sleep 0.1; done
    fi
}

#
# Stop and wait for all given pids to finish
#
stop_and_wait() {
    for pid_to_kill in "$@" ; do
        stop_process "$pid_to_kill"
    done
    for pid_to_wait in "$@" ; do
        wait_for_process "$pid_to_wait"
    done
}

#
# A simple distribution of the `watch` command, because it's not
# standard on all systems
#
watch() {
    (
        # subshell so we can trap and kill the sleep
        trap 'kill $sleep_pid' INT
        while true ; do
            "$@"
            sleep 1 &
            sleep_pid=$!
            wait
        done
    )
}

#
# Add `watch` as a pseudo-command
#
if [ "$1" = "watch" ]; then
    shift
    watch "$0" "$@"
    exit
fi


#
# Find the installation of GVM
#
if [ -z "$GVM_DIR" ]; then
    if [ -d "$HOME/.gvm" ]; then
        GVM_DIR="$HOME/.gvm"
    else
        GVM_DIR="/usr/local/gvm"
    fi
fi

#
# Check for GVM, and if so, make sure it's running
#
if [ -f "$GVM_DIR/bin/gvm" ]; then
    #
    # Configure GVM if not already configured
    #
    if ! which gvm > /dev/null ; then
        . "$GVM_DIR/bin/gvm"
    fi

    #
    # Tell GVM which version we want to use
    #
    if [ -f "$GRADLE_HOME/gvm" ]; then
        gvm use "`cat "$GRADLE_HOME/gvm"`"
    fi
fi

#
# Check for SDKMAN, and if so, make sure it's running
#
if [ -z "$SDKMAN_DIR" ]; then
    if [ -d "$HOME/.sdkman" ]; then
        SDKMAN_DIR="$HOME/.sdkman"
    fi
fi

if [ -f "$SDKMAN_DIR/bin/sdkman-init.sh" ]; then
    #
    # Configure SDKMAN if not already configured
    #
    if ! which sdk > /dev/null ; then
        . "$SDKMAN_DIR/bin/sdkman-init.sh"
    fi

    #
    # Tell SDKMAN which version we want to use
    #
    if [ -f "$GRADLE_HOME/.sdkmanrc" ]; then
        sdk env
    elif [ -f ".sdkmanrc" ]; then
        sdk env
    fi
fi

#
# Split up the JVM_OPTS And GRADLE_OPTS values into an array, following the shell quoting and substitution rules
#
function splitJvmOpts() {
    JVM_OPTS=("$@")
}
eval splitJvmOpts $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS
JVM_OPTS[${#JVM_OPTS[*]}]="-Dorg.gradle.appname=$APP_BASE_NAME"

#
# Stop a gradle process
#
if [ "$1" = "--stop" ]; then
    shift
    "$JAVACMD" \
        "${JVM_OPTS[@]}" \
        -cp "$CLASSPATH" \
        org.gradle.launcher.cli.Main \
        --stop \
        "$@"
    exit
fi

#
# Start a gradle process in the background
#
if [ "$1" = "--start" ]; then
    shift
    run_in_background \
        "$JAVACMD" \
        "${JVM_OPTS[@]}" \
        -cp "$CLASSPATH" \
        $GRADLE_MAIN_CLASS \
        "$@"
    exit
fi

#
# Execute a gradle process
#
exec "$JAVACMD" \
    "${JVM_OPTS[@]}" \
    -cp "$CLASSPATH" \
    $GRADLE_MAIN_CLASS \
    "$@"
