<!-- Opening celebration effects when countdown ends -->
{#if active && effectType !== 'NONE'}
  <div class="opening-effect-container" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 999999; pointer-events: none; overflow: hidden;">
    {#each particles as p}
      <div
        class="particle particle-{effectType.toLowerCase()}"
        style="
          left: {p.x}%;
          top: {p.startY}%;
          --end-y: {p.endY}%;
          --drift: {p.drift}px;
          --size: {p.size}px;
          --color: {p.color};
          --duration: {p.duration}s;
          --delay: {p.delay}s;
          --rotation: {p.rotation}deg;
          --spin: {p.spin}deg;
        ">
      </div>
    {/each}
  </div>
{/if}

<script>
  let { effectType = 'NONE', active = false } = $props();

  const browser = typeof window !== 'undefined';

  const COLORS_CONFETTI = ['#ff577f', '#ff884b', '#ffd384', '#fff9b0', '#3ec70b', '#3b44f6', '#a149fa', '#ff0075', '#00d2d3', '#feca57'];
  const COLORS_FIREWORKS = ['#ff6b6b', '#ffd93d', '#6bcb77', '#4d96ff', '#ff6700', '#c084fc', '#f472b6', '#38bdf8'];
  const COLORS_SPARKLE = ['#ffd700', '#ffffff', '#fffacd', '#f0e68c', '#ffdf00', '#fafad2'];

  let particles = $state([]);

  function generateParticles() {
    if (!browser) return;

    const count = effectType === 'CONFETTI' ? 120 : effectType === 'FIREWORKS' ? 80 : 60;
    const colors = effectType === 'CONFETTI' ? COLORS_CONFETTI : effectType === 'FIREWORKS' ? COLORS_FIREWORKS : COLORS_SPARKLE;

    particles = Array.from({ length: count }, (_, i) => {
      const color = colors[Math.floor(Math.random() * colors.length)];

      if (effectType === 'CONFETTI') {
        return {
          x: Math.random() * 100,
          startY: -10 - Math.random() * 20,
          endY: 110,
          drift: (Math.random() - 0.5) * 200,
          size: 6 + Math.random() * 8,
          color,
          duration: 2.5 + Math.random() * 2,
          delay: Math.random() * 1.5,
          rotation: Math.random() * 360,
          spin: 360 + Math.random() * 720,
        };
      } else if (effectType === 'FIREWORKS') {
        // Burst from center points
        const cx = 20 + Math.random() * 60;
        const cy = 20 + Math.random() * 40;
        const angle = Math.random() * Math.PI * 2;
        const dist = 30 + Math.random() * 40;
        return {
          x: cx,
          startY: cy,
          endY: cy + Math.sin(angle) * dist,
          drift: Math.cos(angle) * dist * 10,
          size: 4 + Math.random() * 6,
          color,
          duration: 1 + Math.random() * 1.5,
          delay: Math.floor(i / 20) * 0.4 + Math.random() * 0.3,
          rotation: 0,
          spin: 0,
        };
      } else {
        // SPARKLE
        return {
          x: Math.random() * 100,
          startY: Math.random() * 100,
          endY: Math.random() * 100,
          drift: 0,
          size: 3 + Math.random() * 6,
          color,
          duration: 0.8 + Math.random() * 1.2,
          delay: Math.random() * 2,
          rotation: 0,
          spin: 0,
        };
      }
    });
  }

  $effect(() => {
    if (active && effectType !== 'NONE' && browser) {
      generateParticles();

      const timeout = setTimeout(() => {
        particles = [];
      }, 5000);

      return () => clearTimeout(timeout);
    }
  });
</script>

<style>
  .particle {
    position: absolute;
    will-change: transform, opacity;
  }

  .particle-confetti {
    width: var(--size);
    height: calc(var(--size) * 0.6);
    background: var(--color);
    border-radius: 2px;
    animation: confetti-fall var(--duration) var(--delay) ease-in forwards;
    opacity: 0;
  }

  @keyframes confetti-fall {
    0% {
      opacity: 1;
      transform: translateY(0) translateX(0) rotate(var(--rotation));
    }
    100% {
      opacity: 0;
      transform: translateY(calc(var(--end-y) * 1vh)) translateX(var(--drift)) rotate(calc(var(--rotation) + var(--spin)));
    }
  }

  .particle-fireworks {
    width: var(--size);
    height: var(--size);
    background: var(--color);
    border-radius: 50%;
    box-shadow: 0 0 6px var(--color), 0 0 12px var(--color);
    animation: firework-burst var(--duration) var(--delay) ease-out forwards;
    opacity: 0;
  }

  @keyframes firework-burst {
    0% {
      opacity: 1;
      transform: translateX(0) translateY(0) scale(0.3);
    }
    30% {
      opacity: 1;
      transform: translateX(calc(var(--drift) * 0.5px)) translateY(calc((var(--end-y) - 50%) * 0.5vh)) scale(1.2);
    }
    100% {
      opacity: 0;
      transform: translateX(var(--drift)) translateY(calc((var(--end-y) - 50%) * 1vh)) scale(0);
    }
  }

  .particle-sparkle {
    width: var(--size);
    height: var(--size);
    background: var(--color);
    border-radius: 50%;
    box-shadow: 0 0 4px var(--color), 0 0 8px var(--color), 0 0 16px var(--color);
    animation: sparkle-twinkle var(--duration) var(--delay) ease-in-out 3 forwards;
    opacity: 0;
  }

  @keyframes sparkle-twinkle {
    0%, 100% {
      opacity: 0;
      transform: scale(0.3);
    }
    50% {
      opacity: 1;
      transform: scale(1);
    }
  }
</style>
