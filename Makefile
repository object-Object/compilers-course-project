examples/%: DEBUG ?= false
examples/%: FORCE
	./gradlew run --quiet --console=plain --args="examples/$*.hexpattern --debug=${DEBUG}"

.PHONY: FORCE
FORCE:
