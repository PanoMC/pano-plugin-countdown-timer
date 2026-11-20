<!--
  Sizes are fluid: the outer element is an inline-size container and every number, box, circle, gap and label is a
  clamp() on its width (cqi), so the four groups always fit on one line, from a narrow phone or sidebar to a wide cover.
-->
<div class="countdown-timer-countdown-display {cssClass}" style="width: 100%; container-type: inline-size;">
  {#if style === 'SIMPLE'}
    <!-- SIMPLE: Plain numbers with colon separators -->
    <div class="countdown-timer-countdown-display__row d-flex justify-content-center align-items-start flex-nowrap" style="gap: {s.gap};">
      {#each groups as group, gi}
        {#if gi > 0}
          <div class="countdown-timer-countdown-display__separator fw-bold" style="opacity: 0.5; {numStyle}">:</div>
        {/if}
        <div class="countdown-timer-countdown-display__unit text-center" style="min-width: 0;">
          <div class="countdown-timer-countdown-display__number fw-bold" style="{numStyle}">{group.value}</div>
          <small class="countdown-timer-countdown-display__label text-uppercase d-block" style="opacity: 0.75; {labelStyle}">{group.label}</small>
        </div>
      {/each}
    </div>

  {:else if style === 'BOXED'}
    <!-- BOXED: Numbers inside rounded boxes -->
    <div class="countdown-timer-countdown-display__row d-flex justify-content-center flex-nowrap" style="gap: {s.gap};">
      {#each groups as group}
        <div class="countdown-timer-countdown-display__unit text-center rounded-3" style="{boxBg} min-width: {s.box}; padding: {s.pad};">
          <div class="countdown-timer-countdown-display__number fw-bold" style="{numStyle}">{group.value}</div>
          <small class="countdown-timer-countdown-display__label text-uppercase d-block mt-1" style="opacity: 0.75; {labelStyle}">{group.label}</small>
        </div>
      {/each}
    </div>

  {:else if style === 'CIRCULAR'}
    <!-- CIRCULAR: Numbers inside circles with ring borders -->
    <div class="countdown-timer-countdown-display__row d-flex justify-content-center flex-nowrap" style="gap: {s.gap};">
      {#each groups as group}
        <div class="countdown-timer-countdown-display__unit d-flex flex-column align-items-center" style="min-width: 0;">
          <div class="countdown-timer-countdown-display__circle rounded-circle d-flex align-items-center justify-content-center"
            style="{circleBorder} width: {s.circle}; height: {s.circle};">
            <span class="countdown-timer-countdown-display__number fw-bold" style="{numStyle}">{group.value}</span>
          </div>
          <small class="countdown-timer-countdown-display__label text-uppercase mt-1" style="opacity: 0.75; {labelStyle}">{group.label}</small>
        </div>
      {/each}
    </div>

  {:else if style === 'MINIMAL'}
    <!-- MINIMAL: Just large numbers inline, no labels -->
    <div class="countdown-timer-countdown-display__row d-flex justify-content-center align-items-center flex-nowrap" style="gap: {s.gap}; font-family: 'Courier New', monospace;">
      {#each groups as group, gi}
        {#if gi > 0}
          <span class="countdown-timer-countdown-display__separator fw-bold" style="opacity: 0.4; {numStyle}">:</span>
        {/if}
        <span class="countdown-timer-countdown-display__number fw-bold" style="{numStyle}">{group.value}</span>
      {/each}
    </div>

  {:else if style === 'SEPARATED'}
    <!-- SEPARATED: Each digit is a separate box (airport/train station style) -->
    <div class="countdown-timer-countdown-display__row d-flex justify-content-center flex-nowrap" style="gap: {s.digitGap};">
      {#each groups as group, gi}
        {#if gi > 0}
          <div class="countdown-timer-countdown-display__separator d-flex align-items-center" style="height: {s.digitH};">
            <span class="fw-bold" style="opacity: 0.4; {numStyle}">:</span>
          </div>
        {/if}
        <div class="countdown-timer-countdown-display__unit d-flex flex-column align-items-center" style="min-width: 0;">
          <div class="d-flex" style="gap: {s.digitGap};">
            {#each group.value.split('') as digit}
              <div class="countdown-timer-countdown-display__digit rounded-2 d-flex align-items-center justify-content-center"
                style="{boxBg} width: {s.digitW}; height: {s.digitH};">
                <span class="countdown-timer-countdown-display__number fw-bold" style="{numStyle}">{digit}</span>
              </div>
            {/each}
          </div>
          <small class="countdown-timer-countdown-display__label text-uppercase mt-1" style="opacity: 0.75; {labelStyle}">{group.label}</small>
        </div>
      {/each}
    </div>
  {/if}
</div>

<script>
  let {
    days = 0,
    hours = 0,
    minutes = 0,
    seconds = 0,
    style = 'SIMPLE',
    size = 'large',
    daysLabel = 'Days',
    hoursLabel = 'Hours',
    minsLabel = 'Minutes',
    secsLabel = 'Seconds',
    isDark = true,
  } = $props();

  function pad(n) {
    return String(Math.max(0, n)).padStart(2, '0');
  }

  // clamp(min, <share of the container width>, max)
  const fluid = (min, cqi, max) => `clamp(${min}, ${cqi}cqi, ${max})`;

  // Per size, per style: number size and the size of the boxes around it. The cqi shares are picked so that
  // four groups, three separators and the gaps stay under 100% of the container width.
  const SIZES = {
    large: {
      num: {
        SIMPLE: fluid('1.25rem', 10.5, '5rem'),
        MINIMAL: fluid('1.25rem', 10, '5rem'),
        BOXED: fluid('1.1rem', 8.5, '4.5rem'),
        CIRCULAR: fluid('1rem', 7, '2.75rem'),
        SEPARATED: fluid('0.9rem', 5.5, '3rem'),
      },
      label: fluid('0.55rem', 2.4, '0.9rem'),
      tracking: fluid('0px', 0.35, '2px'),
      gap: fluid('0.25rem', 2, '1rem'),
      pad: fluid('0.35rem', 2, '1rem'),
      box: fluid('3rem', 20, '120px'),
      circle: fluid('3rem', 20, '110px'),
      digitW: fluid('1.4rem', 7, '60px'),
      digitH: fluid('1.9rem', 9.5, '80px'),
      digitGap: fluid('2px', 1, '0.5rem'),
    },
    small: {
      num: {
        SIMPLE: fluid('0.9rem', 9, '1.25rem'),
        MINIMAL: fluid('0.9rem', 9, '1.25rem'),
        BOXED: fluid('0.85rem', 7, '1.25rem'),
        CIRCULAR: fluid('0.75rem', 6, '1.1rem'),
        SEPARATED: fluid('0.7rem', 5, '1rem'),
      },
      label: fluid('0.55rem', 3.2, '0.7rem'),
      tracking: '0px',
      gap: fluid('0.2rem', 2, '0.5rem'),
      pad: fluid('0.25rem', 2, '0.5rem'),
      box: fluid('2.25rem', 20, '50px'),
      circle: fluid('2.25rem', 20, '52px'),
      digitW: fluid('1.1rem', 7, '28px'),
      digitH: fluid('1.5rem', 9.5, '38px'),
      digitGap: '2px',
    },
  };

  let s = $derived(SIZES[size === 'small' ? 'small' : 'large']);

  let numStyle = $derived(
    `font-size: ${s.num[style] || s.num.SIMPLE}; line-height: 1; font-variant-numeric: tabular-nums; white-space: nowrap;`
  );
  let labelStyle = $derived(
    `font-size: ${s.label}; letter-spacing: ${s.tracking}; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 100%;`
  );
  let cssClass = $derived(size === 'small' ? 'mb-2' : 'mb-4');

  // Box style for BOXED and SEPARATED
  let boxBg = $derived(isDark
    ? 'background: rgba(255,255,255,0.1);'
    : 'background: rgba(0,0,0,0.06);');

  // Circle style
  let circleBorder = $derived(isDark
    ? 'border: 3px solid rgba(255,255,255,0.3); background: rgba(255,255,255,0.05);'
    : 'border: 3px solid rgba(0,0,0,0.15); background: rgba(0,0,0,0.03);');

  let groups = $derived([
    { value: pad(days), label: daysLabel },
    { value: pad(hours), label: hoursLabel },
    { value: pad(minutes), label: minsLabel },
    { value: pad(seconds), label: secsLabel },
  ]);
</script>
