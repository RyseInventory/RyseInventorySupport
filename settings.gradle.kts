rootProject.name = "RyseInventorySupportBot-Parent"

include(":core", ":tag", ":ticket", ":logger")

include(":spring:core")
include(":spring:event")
include(":spring:model")

include(":command-bridge")

include(":punishment")

include(":extension")
include(":document")
