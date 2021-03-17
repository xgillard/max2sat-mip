:

###############################################################################
## On my local machine that would be:
###############################################################################
export GUROBI_HOME="/Library/gurobi903/mac64/lib/"
export CLASSPATH="${GUROBI_HOME}/*:/Users/user/Documents/REPO/max2sat-mip/max2satmip.jar"
export PATH=$PATH:$GUROBI_HOME:.
export LD_LIBRARY_PATH="${GUROBI_HOME}"
###############################################################################
## Burattini
###############################################################################
#export GUROBI_HOME="/home/xgillard/gurobi903/linux64/lib"
#export CLASSPATH="${GUROBI_HOME}/*:/home/xgillard/max2sat-mip/max2satmip.jar"
#export PATH=$PATH:$GUROBI_HOME:.
#export LD_LIBRARY_PATH="${GUROBI_HOME}"
###############################################################################

java -Djava.library.path="${GUROBI_HOME}" -cp "${CLASSPATH}" com.github.xgillard.max2satmip.Main $@
