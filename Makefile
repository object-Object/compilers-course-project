examples/%: DEBUG ?= false
examples/%: FORCE
	./gradlew run --quiet --console=plain --args="examples/$*.hexpattern --debug=${DEBUG}"

.PHONY: debug
debug:
	./gradlew runDebugServer --quiet --console=plain

.PHONY: FORCE
FORCE:
