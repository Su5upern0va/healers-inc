# Pull Request: Resource System Abstraction Layer

## Summary

- Introduces a generic `Resource`/`ResourceNode` abstraction layer for harvestable world resources
- Refactors `HerbNode` to extend the new `ResourceNode<HerbType>` base class
- Updates `Tile` to support any `ResourceNode<?>` while maintaining backward compatibility
- Migrates `WorldGenerator` to use the new generic API

## Motivation

The existing `HerbNode` implementation was tightly coupled to herbs specifically. As we approach the "Herb nodes & harvesting" milestone and look ahead to future resource types (stones, wood, minerals), a generic foundation was needed to:

1. **Reduce code duplication** - Common harvest/regrowth logic now lives in `ResourceNode`
2. **Enable polymorphism** - Tiles can hold any resource type without code changes
3. **Establish patterns** - New resource types follow a clear template

## Changes

### New Files
- `Resource.java` - Marker interface for resource types (herbs, future stones/wood)
- `ResourceNode.java` - Abstract base with yield, regrowth, and harvest mechanics

### Modified Files
- `HerbType.java` - Now implements `Resource`
- `HerbNode.java` - Extends `ResourceNode<HerbType>`, keeps herb-specific `potency`
- `Tile.java` - Generic `ResourceNode<?>` field with deprecated herb-specific methods for compatibility
- `WorldGenerator.java` - Uses `setResourceNode()` instead of `setHerbNode()`

## Backward Compatibility

Existing code using `tile.getHerbNode()`, `tile.setHerbNode()`, and `tile.isHerbNode()` continues to work via deprecated wrapper methods. Migration to the generic API is encouraged but not required.

## Test Plan

- [ ] Visual verification: herb distribution and colors unchanged
- [ ] World generation produces identical results with same seed (69161L)
- [ ] No compilation errors or runtime exceptions

## Future Work

Adding a new resource type (e.g., stones) now requires:
1. Create `StoneType enum implements Resource`
2. Create `StoneNode extends ResourceNode<StoneType>`
3. Add spawn logic in `WorldGenerator`
4. Add rendering in `HealersIncGame`

No changes needed to `Resource`, `ResourceNode`, or `Tile`.
